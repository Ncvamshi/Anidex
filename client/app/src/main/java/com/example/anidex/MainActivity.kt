package com.example.anidex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.anidex.navigation.AppNavigation
import com.example.anidex.ui.theme.AnidexTheme // Your theme name might be different

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnidexTheme {
                // This launches your app's navigation brain
                AppNavigation()
            }
        }
    }
}