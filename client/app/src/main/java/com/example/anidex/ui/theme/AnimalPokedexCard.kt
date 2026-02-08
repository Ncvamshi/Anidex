package com.example.anidex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.anidex.data.Animal

@Composable
fun AnimalPokedexCard(animal: Animal) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFFDECEC))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(350.dp),
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(animal.localUri ?: animal.imageUrl)
                    .addHeader("User-Agent", "Mozilla/5.0") // Wikipedia Fix
                    .crossfade(true)
                    .build(),
                contentDescription = animal.name,
                modifier = Modifier.fillMaxSize().background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = animal.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text(text = animal.scientificName ?: "Scientific name unknown", color = Color.Gray)
                }
                Surface(color = Color.White, shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp) {
                    Text(
                        text = animal.category ?: "Species",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold, color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBox("Weight", animal.maxWeight ?: "N/A", Icons.Default.Scale)
                StatBox("Height", animal.maxHeight ?: "N/A", Icons.Default.Height)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = animal.description ?: "No description available.", color = Color.DarkGray)

            Spacer(modifier = Modifier.height(24.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoSection("Habitat", animal.habitat ?: "Unknown", Icons.Default.Landscape)
                    InfoSection("Diet", animal.diet ?: "Unknown", Icons.Default.Restaurant)
                    InfoSection("Lifestyle", animal.lifestyle ?: "Unknown", Icons.Default.Timer)

                    if (!animal.colors.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Recorded Colors:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            animal.colors.forEach { colorName ->
                                Box(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(parseColor(colorName))
                                        .background(Color.Black.copy(alpha = 0.05f))
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B).copy(alpha = 0.2f)), shape = RoundedCornerShape(16.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFF57F17))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Fun Fact", fontWeight = FontWeight.Bold, color = Color(0xFFF57F17))
                        Text(animal.funFact ?: "Truly unique biometric markers.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun StatBox(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = Color.Red, modifier = Modifier.size(24.dp))
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun InfoSection(title: String, content: String, icon: ImageVector) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = Color.Red, style = MaterialTheme.typography.labelMedium)
            Text(content, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        }
    }
}

fun parseColor(colorName: String): Color {
    return when (colorName.lowercase().trim()) {
        "brown" -> Color(0xFF8B4513)
        "black" -> Color(0xFF000000)
        "white" -> Color(0xFFFFFFFF)
        "gray", "grey" -> Color.Gray
        "green" -> Color(0xFF4CAF50)
        "yellow" -> Color(0xFFFBC02D)
        "red" -> Color(0xFFD32F2F)
        "orange" -> Color(0xFFFF9800)
        "blue" -> Color(0xFF1976D2)
        "tan" -> Color(0xFFD2B48C)
        "olive" -> Color(0xFF808000)
        "cream" -> Color(0xFFFFFDD0)
        else -> Color.Transparent
    }
}