package com.example.anidex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.anidex.ui.LoginScreen
import com.example.anidex.ui.RegisterScreen
import com.example.anidex.ui.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // This matches the user_id from your predict.bru file
    val testUserId = "d8cf2785-2e9f-467c-b950-98b6ec07db68"

    NavHost(navController = navController, startDestination = "login") {
        // Login Page
        composable("login") {
            LoginScreen(navController)
        }

        // Registration Page
        composable("register") {
            RegisterScreen(navController)
        }

        // Main Hub (Camera, Search, Collection)
        composable("main") {
            MainScreen(navController = navController)
        }
    }
}