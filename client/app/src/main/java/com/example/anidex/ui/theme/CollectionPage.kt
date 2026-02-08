package com.example.anidex.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.anidex.data.Animal
import com.example.anidex.data.RetrofitClient
import java.io.File

@Composable
fun CollectionPage(userId: String) {
    val context = LocalContext.current
    var fullCollection by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedAnimal by remember { mutableStateOf<Animal?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val displayedAnimals = fullCollection.filter { it.name.contains(searchQuery, ignoreCase = true) }

    LaunchedEffect(Unit) {
        try {
            val responseList = RetrofitClient.instance.getCollection(userId)
            fullCollection = responseList.map { wrapper ->
                val animal = wrapper.animal
                animal.copy(localUri = findLocalImage(context, animal.name))
            }
        } catch (e: Exception) {
            Log.e("ANIDEX", "Collection Fetch Failed: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    if (selectedAnimal != null) {
        Box {
            AnimalPokedexCard(selectedAnimal!!)
            IconButton(
                onClick = { selectedAnimal = null },
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.Black)
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Your Collection", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search your species...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp)
            )

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayedAnimals) { animal ->
                        CollectionGridItem(animal) { selectedAnimal = animal }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionGridItem(animal: Animal, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(
                model = animal.localUri ?: animal.imageUrl,
                contentDescription = animal.name,
                modifier = Modifier.height(140.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = animal.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

// Fixed: Now searches specifically for the animal's name in the filename
fun findLocalImage(context: Context, animalName: String): String? {
    val capturesDir = File(context.filesDir, "captures")
    if (!capturesDir.exists()) return null

    val safeName = animalName.replace(" ", "_")
    val files = capturesDir.listFiles()

    // Sort by last modified so we get the most recent capture of this specific animal
    return files?.filter { it.name.startsWith(safeName, ignoreCase = true) }
        ?.maxByOrNull { it.lastModified() }
        ?.absolutePath
}