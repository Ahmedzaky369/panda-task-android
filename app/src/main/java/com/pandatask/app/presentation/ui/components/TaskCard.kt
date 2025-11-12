package com.pandatask.app.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.pandatask.app.domain.model.Task
import com.pandatask.app.presentation.ui.theme.CustomShapes
import com.pandatask.app.presentation.ui.theme.extendedColors

/**
 * Task card component matching PWA design
 * Replicates the TaskCard component from PWA
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit,
    onRemove: (() -> Unit)? = null,
    showGrip: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    showCheckbox: Boolean = true,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (task.completed) 0.5f else 1f,
        label = "task_alpha"
    )
    
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    
    val borderStroke = if (isSelected) {
        BorderStroke(2.dp, borderColor)
    } else {
        BorderStroke(1.dp, MaterialTheme.extendedColors.selectionBorder.copy(alpha = 0.1f))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable { onClick?.invoke() },
        shape = CustomShapes.TaskCard,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.extendedColors.selectionBackground
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Grip handle for drag and drop (if enabled)
            if (showGrip) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Grip icon placeholder - would use actual grip icon
                    Icon(
                        imageVector = Icons.Default.Delete, // Placeholder
                        contentDescription = "Drag handle",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Checkbox
            if (showCheckbox) {
                IconButton(
                    onClick = { onToggleComplete() },
                    modifier = Modifier.size(32.dp)
                ) {
                    if (task.completed) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Mark incomplete",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Circle,
                            contentDescription = "Mark complete",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Task text
            Text(
                text = task.text,
                style = MaterialTheme.typography.titleMedium.copy(
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else if (task.completed) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
                modifier = Modifier.weight(1f)
            )

            // Remove button
            AnimatedVisibility(
                visible = onRemove != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                IconButton(
                    onClick = { onRemove?.invoke() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}