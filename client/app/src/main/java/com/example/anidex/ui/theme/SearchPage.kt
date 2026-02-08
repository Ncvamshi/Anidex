package com.example.anidex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.anidex.data.Animal
import com.example.anidex.data.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun SearchPage() {
    var query by remember { mutableStateOf("") }
    var animals by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var selectedAnimal by remember { mutableStateOf<Animal?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val performSearch = {
        scope.launch {
            if (query.isNotBlank()) {
                isLoading = true
                keyboardController?.hide()
                try {
                    animals = RetrofitClient.instance.searchAnimals(query)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    if (selectedAnimal != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimalPokedexCard(selectedAnimal!!)

            SmallFloatingActionButton(
                onClick = { selectedAnimal = null },
                modifier = Modifier.padding(16.dp),
                containerColor = Color.White.copy(alpha = 0.9f)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                "Encyclopedia",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search by name or category") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { performSearch() }),
                trailingIcon = {
                    IconButton(onClick = { performSearch() }) {
                        if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        else Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            if (animals.isEmpty() && !isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Enter a name (e.g., 'Tiger') to start", color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(animals) { animal ->
                        AnimalGridItem(animal) { selectedAnimal = animal }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalGridItem(animal: Animal, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(animal.imageUrl)
                    .addHeader("User-Agent", "Mozilla/5.0") // Wikipedia Fix
                    .crossfade(true)
                    .build(),
                contentDescription = animal.name,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = animal.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = animal.category ?: "Unknown",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}