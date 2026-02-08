//package com.example.anidex.ui
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.anidex.data.SessionManager
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen(navController: NavController) {
//    val context = LocalContext.current
//    val sessionManager = remember { SessionManager(context) }
//
//    // UI States
//    var currentTab by remember { mutableStateOf("scanner") }
//    var accountMenuExpanded by remember { mutableStateOf(false) }
//
//    // Get current ID and all logged-in accounts
//    var activeUserId by remember { mutableStateOf(sessionManager.getCurrentUserId() ?: "") }
//    val allUsers = sessionManager.getAllUsers().toList()
//
//    // Safety check: if no one is logged in, force go to login
//    if (activeUserId.isEmpty()) {
//        LaunchedEffect(Unit) {
//            navController.navigate("login") { popUpTo(0) }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Column {
//                        Text("Anidex", style = MaterialTheme.typography.titleLarge)
//                        Text("User: $activeUserId", style = MaterialTheme.typography.bodySmall)
//                    }
//                },
//                actions = {
//                    Box {
//                        IconButton(onClick = { accountMenuExpanded = true }) {
//                            Icon(Icons.Default.AccountCircle, contentDescription = "Accounts")
//                        }
//
//                        DropdownMenu(
//                            expanded = accountMenuExpanded,
//                            onDismissRequest = { accountMenuExpanded = false }
//                        ) {
//                            Text("Switch Account", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelLarge)
//
//                            allUsers.forEach { user ->
//                                DropdownMenuItem(
//                                    text = { Text(user) },
//                                    onClick = {
//                                        sessionManager.switchUser(user)
//                                        activeUserId = user // Update UI state
//                                        accountMenuExpanded = false
//                                    },
//                                    trailingIcon = {
//                                        if (user == activeUserId) Icon(Icons.Default.Check, "Current")
//                                    }
//                                )
//                            }
//
//                            HorizontalDivider()
//
//                            DropdownMenuItem(
//                                text = { Text("Log Out Current") },
//                                onClick = {
//                                    sessionManager.logoutCurrent()
//                                    navController.navigate("login") { popUpTo(0) }
//                                },
//                                leadingIcon = { Icon(Icons.Default.Logout, null) }
//                            )
//                        }
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer
//                )
//            )
//        },
//        bottomBar = {
//            NavigationBar {
//                NavigationBarItem(
//                    selected = currentTab == "search",
//                    onClick = { currentTab = "search" },
//                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
//                    label = { Text("Search") }
//                )
//                NavigationBarItem(
//                    selected = currentTab == "scanner",
//                    onClick = { currentTab = "scanner" },
//                    icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Scan") },
//                    label = { Text("Scan") }
//                )
//                NavigationBarItem(
//                    selected = currentTab == "collection",
//                    onClick = { currentTab = "collection" },
//                    icon = { Icon(Icons.Default.GridView, contentDescription = "My Collection") },
//                    label = { Text("Collection") }
//                )
//            }
//        }
//    ) { padding ->
//        Box(modifier = Modifier.padding(padding)) {
//            // Note: We use activeUserId here so sub-pages refresh when we switch accounts
//            when (currentTab) {
//                "search" -> SearchPage() // Assuming SearchPage doesn't need ID
//                "scanner" -> ScannerPage(activeUserId)
//                "collection" -> CollectionPage(activeUserId)
//            }
//        }
//    }
//}

package com.example.anidex.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.anidex.data.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var currentTab by remember { mutableStateOf("scanner") }
    var menuExpanded by remember { mutableStateOf(false) }
    var activeUserId by remember { mutableStateOf(sessionManager.getCurrentUserId() ?: "") }

    // Kick to login if session is empty
    if (activeUserId.isEmpty()) {
        LaunchedEffect(Unit) {
            navController.navigate("login") { popUpTo(0) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anidex", fontWeight = FontWeight.Bold) },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            // Header showing current Username
                            Text(
                                text = sessionManager.getUsername(activeUserId),
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(16.dp, 8.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider()

                            // Account Switching
                            sessionManager.getAllUsers().forEach { userId ->
                                DropdownMenuItem(
                                    text = { Text(sessionManager.getUsername(userId)) },
                                    onClick = {
                                        sessionManager.switchUser(userId)
                                        activeUserId = userId
                                        menuExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            if (userId == activeUserId) Icons.Default.CheckCircle
                                            else Icons.Default.RadioButtonUnchecked,
                                            null
                                        )
                                    }
                                )
                            }

                            HorizontalDivider()

                            // Logout
                            DropdownMenuItem(
                                text = { Text("Log Out") },
                                onClick = {
                                    sessionManager.logoutCurrent()
                                    navController.navigate("login") { popUpTo(0) }
                                },
                                leadingIcon = {Icon(Icons.Default.Logout, null) }
                            )
                        }
                    }
                },
                // FIX: This forces the TopAppBar to match your app's primary theme color
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == "search",
                    onClick = { currentTab = "search" },
                    icon = { Icon(Icons.Default.Search, null) },
                    label = { Text("Search") }
                )
                NavigationBarItem(
                    selected = currentTab == "scanner",
                    onClick = { currentTab = "scanner" },
                    icon = { Icon(Icons.Default.CameraAlt, null) },
                    label = { Text("Scan") }
                )
                NavigationBarItem(
                    selected = currentTab == "collection",
                    onClick = { currentTab = "collection" },
                    icon = { Icon(Icons.Default.GridView, null) },
                    label = { Text("Collection") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentTab) {
                "search" -> SearchPage()
                "scanner" -> ScannerPage(activeUserId)
                "collection" -> CollectionPage(activeUserId)
            }
        }
    }
}