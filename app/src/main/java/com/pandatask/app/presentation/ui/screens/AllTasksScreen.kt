package com.pandatask.app.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pandatask.app.domain.model.Category
import com.pandatask.app.domain.model.Task
import com.pandatask.app.presentation.ui.components.PandaFloatingActionButton
import com.pandatask.app.presentation.ui.components.TaskCard
import com.pandatask.app.presentation.viewmodel.MainViewModel

/**
 * All Tasks screen - displays tasks organized by categories
 * Mirrors the AllTasksScreen component from PWA
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTasksScreen(
    onAddTask: () -> Unit,
    onManageCategories: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val todayTasks by viewModel.todayTasks.collectAsStateWithLifecycle()
    val selectedTaskIds by viewModel.selectedTaskIds.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    
    var collapsedCategories by remember { mutableStateOf(setOf<String>()) }
    
    val accentColor = remember(settings.accentColor) {
        Color(android.graphics.Color.parseColor(settings.accentColor))
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 88.dp) // Space for FAB
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All Tasks",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                IconButton(onClick = onManageCategories) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Manage categories",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Text(
                text = "Organize your life",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Selection toolbar
            AnimatedVisibility(
                visible = selectedTaskIds.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                SelectionToolbar(
                    selectedCount = selectedTaskIds.size,
                    onAddToToday = viewModel::addSelectedToToday,
                    onRemoveFromToday = viewModel::removeSelectedFromToday,
                    onEdit = { /* Handle edit */ },
                    accentColor = accentColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Categories and tasks
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                itemsIndexed(
                    items = categories,
                    key = { _, category -> category.id }
                ) { index, category ->
                    val categoryTasks = allTasks.filter { it.categoryId == category.id }
                    
                    if (categoryTasks.isNotEmpty()) {
                        CategorySection(
                            category = category,
                            tasks = categoryTasks,
                            isCollapsed = collapsedCategories.contains(category.id),
                            onToggleCollapse = { 
                                collapsedCategories = if (collapsedCategories.contains(category.id)) {
                                    collapsedCategories - category.id
                                } else {
                                    collapsedCategories + category.id
                                }
                            },
                            onTaskClick = viewModel::toggleTaskSelection,
                            onDeleteTask = viewModel::deleteTask,
                            selectedTaskIds = selectedTaskIds,
                            accentColor = accentColor,
                            showDivider = index > 0
                        )
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

@Composable
private fun SelectionToolbar(
    selectedCount: Int,
    onAddToToday: () -> Unit,
    onRemoveFromToday: () -> Unit,
    onEdit: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "$selectedCount task${if (selectedCount > 1) "s" else ""} selected",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Add to Today button
                Button(
                    onClick = onAddToToday,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add to Today", color = Color.White)
                }
                
                // Remove from Today button
                OutlinedButton(
                    onClick = onRemoveFromToday,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Remove")
                }
                
                // Edit button
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CategorySection(
    category: Category,
    tasks: List<Task>,
    isCollapsed: Boolean,
    onToggleCollapse: () -> Unit,
    onTaskClick: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
    selectedTaskIds: Set<String>,
    accentColor: Color,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Category divider
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 32.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
        
        // Category card with glow effect (matches PWA)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Category header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category name with animated border
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = accentColor.copy(alpha = 0.1f)
                        ),
                        border = BorderStroke(2.dp, accentColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            // Task count badge
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = accentColor.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = tasks.size.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = accentColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    
                    // Collapse/expand button
                    IconButton(onClick = onToggleCollapse) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isCollapsed) "Expand" else "Collapse",
                            tint = accentColor,
                            modifier = Modifier.rotate(if (isCollapsed) -90f else 0f)
                        )
                    }
                }
                
                // Tasks list
                AnimatedVisibility(
                    visible = !isCollapsed,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tasks.forEachIndexed { taskIndex, task ->
                            TaskListItem(
                                task = task,
                                isSelected = selectedTaskIds.contains(task.id),
                                onClick = { onTaskClick(task.id) },
                                onDelete = { onDeleteTask(task.id) },
                                accentColor = accentColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskListItem(
    task: Task,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                accentColor.copy(alpha = 0.15f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, accentColor.copy(alpha = 0.5f))
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit, // Use delete icon
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}