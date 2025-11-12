package com.pandatask.app.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pandatask.app.domain.model.Task
import com.pandatask.app.presentation.ui.components.PandaFloatingActionButton
import com.pandatask.app.presentation.ui.components.TaskCard
import com.pandatask.app.presentation.viewmodel.MainViewModel
import org.burnoutcrew.reorderable.*

/**
 * Today screen - displays selected daily tasks
 * Mirrors the TodayScreen component from PWA
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val todayTasks by viewModel.todayTasks.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    
    // Separate completed and incomplete tasks (matches PWA logic)
    val incompleteTasks = remember(todayTasks) {
        todayTasks.filter { !it.completed }
    }
    val completedTasks = remember(todayTasks) {
        todayTasks.filter { it.completed }
    }
    
    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            // Handle reordering logic
            val fromIndex = from.index
            val toIndex = to.index
            if (fromIndex != toIndex) {
                val reorderedIds = incompleteTasks.toMutableList().apply {
                    add(toIndex, removeAt(fromIndex))
                }.map { it.id }
                viewModel.reorderTodayTasks(reorderedIds)
            }
        }
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 88.dp) // Space for FAB
        ) {
            // Header
            Column(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Focus on what matters",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Content
            if (incompleteTasks.isEmpty() && completedTasks.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "What's your vibe today?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = reorderableState.listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.reorderable(reorderableState)
                ) {
                    // Incomplete tasks (reorderable)
                    items(
                        items = incompleteTasks,
                        key = { task -> task.id }
                    ) { task ->
                        ReorderableItem(
                            reorderableState = reorderableState,
                            key = task.id
                        ) { isDragging ->
                            TaskCard(
                                task = task,
                                onToggleComplete = { 
                                    viewModel.toggleTaskComplete(task.id, isToday = true) 
                                },
                                onRemove = { 
                                    viewModel.removeFromToday(task.id) 
                                },
                                showGrip = true,
                                modifier = Modifier
                                    .detectReorderAfterLongPress(reorderableState)
                                    .animateItemPlacement()
                            )
                        }
                    }

                    // Separator for completed tasks
                    if (completedTasks.isNotEmpty()) {
                        item {
                            CompletedTasksSeparator(
                                accentColor = androidx.compose.ui.graphics.Color(
                                    android.graphics.Color.parseColor(settings.accentColor)
                                )
                            )
                        }

                        // Completed tasks (non-reorderable)
                        items(
                            items = completedTasks,
                            key = { task -> "completed_${task.id}" }
                        ) { task ->
                            TaskCard(
                                task = task,
                                onToggleComplete = { 
                                    viewModel.toggleTaskComplete(task.id, isToday = true) 
                                },
                                onRemove = { 
                                    viewModel.removeFromToday(task.id) 
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button
        PandaFloatingActionButton(
            onClick = onAddTask,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

/**
 * Separator component for completed tasks section
 * Matches the PWA's "finished" separator design
 */
@Composable
private fun CompletedTasksSeparator(
    accentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = accentColor.copy(alpha = 0.3f),
            thickness = 1.dp
        )
        
        Text(
            text = "finished",
            style = MaterialTheme.typography.labelMedium,
            color = accentColor.copy(alpha = 0.7f)
        )
        
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = accentColor.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}