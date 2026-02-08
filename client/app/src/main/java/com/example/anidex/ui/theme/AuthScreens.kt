//package com.example.anidex.ui
//
//import android.util.Log
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.anidex.data.AuthRequest
//import com.example.anidex.data.RetrofitClient
//import com.example.anidex.data.SessionManager
//import kotlinx.coroutines.launch
//
//@Composable
//fun LoginScreen(navController: NavController) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val sessionManager = SessionManager(context)
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(24.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Anidex Login", style = MaterialTheme.typography.headlineLarge)
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(
//            onClick = {
//                Log.d("ANIDEX_DEBUG", "Login button clicked. Username: $username")
//                scope.launch {
//                    try {
//                        val response = RetrofitClient.instance.login(AuthRequest(username, password))
//                        Log.d("ANIDEX_DEBUG", "Success! Received UserID: ${response.userId}")
//                        sessionManager.saveUserId(response.userId)
//                        navController.navigate("main")
//                    } catch (e: retrofit2.HttpException) {
//                        val errorBody = e.response()?.errorBody()?.string()
//                        Log.e("ANIDEX_DEBUG", "HTTP Error: ${e.code()} | Body: $errorBody")
//                    } catch (e: Exception) {
//                        Log.e("ANIDEX_DEBUG", "Connection/Unexpected Error: ${e.localizedMessage}")
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
//        ) {
//            Text("Sign In")
//        }
//
//        TextButton(onClick = { navController.navigate("register") }) {
//            Text("Don't have an account? Create one")
//        }
//    }
//}
//
//@Composable
//fun RegisterScreen(navController: NavController) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(32.dp))
//
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Choose Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(
//            onClick = {
//                Log.d("ANIDEX_DEBUG", "Register button clicked for: $username")
//                scope.launch {
//                    try {
//                        val response = RetrofitClient.instance.register(AuthRequest(username, password))
//                        Log.d("ANIDEX_DEBUG", "Registration Successful: ${response.message}")
//                        navController.navigate("login")
//                    } catch (e: Exception) {
//                        Log.e("ANIDEX_DEBUG", "Registration Failed: ${e.localizedMessage}")
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
//        ) {
//            Text("Sign Up")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextButton(onClick = { navController.navigate("login") }) {
//            Text("Already have an account? Login")
//        }
//    }
//}

//v2:
//package com.example.anidex.ui
//
//import android.util.Log
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.anidex.data.AuthRequest
//import com.example.anidex.data.RetrofitClient
//import com.example.anidex.data.SessionManager
//import kotlinx.coroutines.launch
//
//@Composable
//fun LoginScreen(navController: NavController) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val sessionManager = SessionManager(context)
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(24.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Anidex Login", style = MaterialTheme.typography.headlineLarge)
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Inside LoginScreen (authscreen.kt), update your button onClick:
//        Button(
//            onClick = {
//                scope.launch {
//                    try {
//                        val response = RetrofitClient.instance.login(AuthRequest(username, password))
//                        // Pass BOTH the ID and the Username here
//                        sessionManager.saveUserId(response.userId, username)
//                        navController.navigate("main") { popUpTo("login") { inclusive = true } }
//                    } catch (e: Exception) {
//                        Log.e("ANIDEX_DEBUG", "Login Failed: ${e.localizedMessage}")
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
//        ) {
//            Text("Sign In")
//        }
//
//        TextButton(onClick = { navController.navigate("register") }) {
//            Text("Don't have an account? Create one")
//        }
//    }
//}
//
//@Composable
//fun RegisterScreen(navController: NavController) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(32.dp))
//
//        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Choose Username") }, modifier = Modifier.fillMaxWidth())
//        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
//
//        Button(
//            onClick = {
//                scope.launch {
//                    try {
//                        RetrofitClient.instance.register(AuthRequest(username, password))
//                        navController.navigate("login")
//                    } catch (e: Exception) {
//                        Log.e("ANIDEX_DEBUG", "Reg Failed: ${e.localizedMessage}")
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
//        ) {
//            Text("Sign Up")
//        }
//
//        TextButton(onClick = { navController.navigate("login") }) {
//            Text("Already have an account? Login")
//        }
//    }
//}

package com.example.anidex.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.anidex.data.AuthRequest
import com.example.anidex.data.RetrofitClient
import com.example.anidex.data.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Anidex Login", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = null },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null
            )

            // Display Error Message in Red
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                        return@Button
                    }
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.login(AuthRequest(username, password))
                            sessionManager.saveUserId(response.userId, username)

                            // Success Feedback
                            snackbarHostState.showSnackbar("Logged in successfully!")
                            delay(500) // Brief pause so they see the message
                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        } catch (e: Exception) {
                            errorMessage = "Invalid username or password"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Sign In")
            }

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Don't have an account? Create one")
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = null },
                label = { Text("Choose Username") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (username.length < 3) {
                        errorMessage = "Username too short"
                        return@Button
                    }
                    scope.launch {
                        try {
                            RetrofitClient.instance.register(AuthRequest(username, password))

                            // Show Success Popup (Snackbar)
                            val result = snackbarHostState.showSnackbar(
                                message = "Registered successfully!",
                                actionLabel = "Login Now",
                                duration = SnackbarDuration.Short
                            )
                            // Navigate after they see the message or click the action
                            navController.navigate("login")
                        } catch (e: Exception) {
                            errorMessage = "Registration failed. Try a different username."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Sign Up")
            }

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login")
            }
        }
    }
}