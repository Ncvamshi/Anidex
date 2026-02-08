package com.example.anidex.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.anidex.data.Animal
import com.example.anidex.data.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@Composable
fun ScannerPage(userId: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var detectedAnimal by remember { mutableStateOf<Animal?>(null) }

    val cameraTempFile = remember { File(context.cacheDir, "camera_raw.jpg") }

    // Updated: Now accepts animalName to make the filename specific
    fun saveImagePermanently(animalName: String, sourceFile: File): String {
        val capturesDir = File(context.filesDir, "captures").apply { if (!exists()) mkdirs() }
        val safeName = animalName.replace(" ", "_")
        val fileName = "${safeName}_${System.currentTimeMillis()}.jpg"
        val permanentFile = File(capturesDir, fileName)

        sourceFile.inputStream().use { input ->
            permanentFile.outputStream().use { output -> input.copyTo(output) }
        }
        return permanentFile.absolutePath
    }

    fun uploadAndPredict(file: File) {
        isLoading = true
        scope.launch {
            try {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val response = RetrofitClient.instance.predict(userId, body)

                // Now we save it permanently with the correct name from the API
                val permanentPath = saveImagePermanently(response.animal.name, file)

                detectedAnimal = response.animal.copy(localUri = permanentPath)
                Log.d("ANIDEX_DEBUG", "Saved as: $permanentPath")
            } catch (e: Exception) {
                Log.e("ANIDEX_DEBUG", "Upload Error: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { input ->
                FileOutputStream(cameraTempFile).use { output -> input.copyTo(output) }
            }
            uploadAndPredict(cameraTempFile)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) uploadAndPredict(cameraTempFile)
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) cameraLauncher.launch(cameraTempFile.getUri(context))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            PokedexLoadingAnimation()
        } else if (detectedAnimal != null) {
            Column(Modifier.fillMaxSize()) {
                TextButton(onClick = { detectedAnimal = null }, modifier = Modifier.padding(16.dp)) {
                    Text("← RESCAN", color = Color.Red)
                }
                AnimalPokedexCard(detectedAnimal!!)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Capture It!", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(40.dp))
                Button(
                    onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                    modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("USE CAMERA") }
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(0.7f).height(56.dp)
                ) { Text("UPLOAD FROM FILES") }
            }
        }
    }
}

fun File.getUri(context: Context): Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", this)

@Composable
fun PokedexLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse), label = ""
    )
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        CircularProgressIndicator(Modifier.size(80.dp), color = Color.Red, strokeWidth = 8.dp)
        Spacer(Modifier.height(24.dp))
        Text("ANALYZING SPECIMEN...", modifier = Modifier.graphicsLayer { this.alpha = alpha })
    }
}