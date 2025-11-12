package com.pandatask.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pandatask.app.domain.model.Goal
import com.pandatask.app.domain.model.GoalType
import com.pandatask.app.presentation.ui.components.GoalCard
import com.pandatask.app.presentation.ui.components.LabeledProgressIndicator
import com.pandatask.app.presentation.viewmodel.MainViewModel
import org.burnoutcrew.reorderable.*

/**
 * Plan screen - displays weekly and monthly goals with progress tracking
 * Mirrors the PlanScreen component from PWA
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    onShowNotes: () -> Unit,
    onAddWeeklyGoal: () -> Unit,
    onAddMonthlyGoal: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val weeklyGoals by viewModel.weeklyGoals.collectAsStateWithLifecycle()
    val monthlyGoals by viewModel.monthlyGoals.collectAsStateWithLifecycle()
    val weeklyProgress by viewModel.weeklyProgress.collectAsStateWithLifecycle()
    val monthlyProgress by viewModel.monthlyProgress.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    
    val accentColor = remember(settings.accentColor) {
        Color(android.graphics.Color.parseColor(settings.accentColor))
    }
    
    // Separate completed and incomplete goals
    val weeklyCompleted = remember(weeklyGoals) { weeklyGoals.filter { it.completed } }
    val weeklyIncomplete = remember(weeklyGoals) { weeklyGoals.filter { !it.completed } }
    val monthlyCompleted = remember(monthlyGoals) { monthlyGoals.filter { it.completed } }
    val monthlyIncomplete = remember(monthlyGoals) { monthlyGoals.filter { !it.completed } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Sticky header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Plan",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    IconButton(onClick = onShowNotes) {
                        Icon(
                            imageVector = Icons.Default.Note,
                            contentDescription = "Notes",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Text(
                    text = "Your goals, your way",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Scrollable content
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Weekly Goals Section
            item {
                GoalSection(
                    title = "Weekly Goals",
                    incompleteGoals = weeklyIncomplete,
                    completedGoals = weeklyCompleted,
                    progress = weeklyProgress,
                    onToggleGoal = viewModel::toggleGoalComplete,
                    onAddGoal = onAddWeeklyGoal,
                    accentColor = accentColor
                )
            }
            
            // Monthly Goals Section  
            item {
                GoalSection(
                    title = "Monthly Goals",
                    incompleteGoals = monthlyIncomplete,
                    completedGoals = monthlyCompleted,
                    progress = monthlyProgress,
                    onToggleGoal = viewModel::toggleGoalComplete,
                    onAddGoal = onAddMonthlyGoal,
                    accentColor = accentColor
                )
            }
            
            // Bottom padding for last item
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun GoalSection(
    title: String,
    incompleteGoals: List<Goal>,
    completedGoals: List<Goal>,
    progress: Float,
    onToggleGoal: (String) -> Unit,
    onAddGoal: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            // Handle reordering if needed
        }
    )
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section header with progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${completedGoals.size} / ${incompleteGoals.size + completedGoals.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                IconButton(
                    onClick = onAddGoal,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add goal",
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        // Progress indicator
        LabeledProgressIndicator(
            label = "",
            completed = completedGoals.size,
            total = incompleteGoals.size + completedGoals.size,
            progress = progress,
            color = accentColor
        )
        
        // Incomplete goals
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            incompleteGoals.forEach { goal ->
                GoalCard(
                    goal = goal,
                    onToggleComplete = { onToggleGoal(goal.id) }
                )
            }
        }
        
        // Completed goals
        if (completedGoals.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                completedGoals.forEach { goal ->
                    GoalCard(
                        goal = goal,
                        onToggleComplete = { onToggleGoal(goal.id) }
                    )
                }
            }
        }
    }
}