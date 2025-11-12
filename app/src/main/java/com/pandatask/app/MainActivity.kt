package com.pandatask.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pandatask.app.presentation.ui.navigation.PandaBottomNavigation
import com.pandatask.app.presentation.ui.navigation.Screen
import com.pandatask.app.presentation.ui.screens.*
import com.pandatask.app.presentation.ui.theme.PandaTaskTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Single Activity with Jetpack Compose
 * Hosts the entire app navigation and main screens
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PandaTaskTheme {
                PandaTaskApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PandaTaskApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.Today.route
    
    // Modal states
    var showAddTaskModal by remember { mutableStateOf(false) }
    var showAddTodayTaskModal by remember { mutableStateOf(false) }
    var showManageCategoriesModal by remember { mutableStateOf(false) }
    var showNotesModal by remember { mutableStateOf(false) }
    var showAddWeeklyGoalModal by remember { mutableStateOf(false) }
    var showAddMonthlyGoalModal by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            PandaBottomNavigation(
                selectedTab = currentRoute,
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Today.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Today.route) {
                TodayScreen(
                    onAddTask = { showAddTodayTaskModal = true }
                )
            }
            
            composable(Screen.AllTasks.route) {
                AllTasksScreen(
                    onAddTask = { showAddTaskModal = true },
                    onManageCategories = { showManageCategoriesModal = true }
                )
            }
            
            composable(Screen.Plan.route) {
                PlanScreen(
                    onShowNotes = { showNotesModal = true },
                    onAddWeeklyGoal = { showAddWeeklyGoalModal = true },
                    onAddMonthlyGoal = { showAddMonthlyGoalModal = true }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
    
    // Modal dialogs
    if (showAddTaskModal) {
        AddTaskModal(
            onDismiss = { showAddTaskModal = false }
        )
    }
    
    if (showAddTodayTaskModal) {
        AddTodayTaskModal(
            onDismiss = { showAddTodayTaskModal = false }
        )
    }
    
    if (showManageCategoriesModal) {
        ManageCategoriesModal(
            onDismiss = { showManageCategoriesModal = false }
        )
    }
    
    if (showNotesModal) {
        NotesModal(
            onDismiss = { showNotesModal = false }
        )
    }
    
    if (showAddWeeklyGoalModal) {
        AddGoalModal(
            title = "Add Weekly Goal",
            placeholder = "What's your weekly goal?",
            onAdd = { /* Handle add weekly goal */ },
            onDismiss = { showAddWeeklyGoalModal = false }
        )
    }
    
    if (showAddMonthlyGoalModal) {
        AddGoalModal(
            title = "Add Monthly Goal", 
            placeholder = "What's your monthly goal?",
            onAdd = { /* Handle add monthly goal */ },
            onDismiss = { showAddMonthlyGoalModal = false }
        )
    }
}

// Placeholder modal composables - would be implemented with full functionality
@Composable
private fun AddTaskModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = { Text("Add task modal would be implemented here") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun AddTodayTaskModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task to Today") },
        text = { Text("Add today task modal would be implemented here") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun ManageCategoriesModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Categories") },
        text = { Text("Category management modal would be implemented here") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun NotesModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notes") },
        text = { Text("Notes modal would be implemented here") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun AddGoalModal(
    title: String,
    placeholder: String,
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text("$placeholder modal would be implemented here") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}