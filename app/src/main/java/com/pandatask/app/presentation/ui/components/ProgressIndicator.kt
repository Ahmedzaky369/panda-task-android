package com.pandatask.app.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pandatask.app.presentation.ui.theme.CustomShapes

/**
 * Custom progress indicator matching PWA design
 * Replicates the progress bars from the Plan screen
 */
@Composable
fun PandaProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600), // Matches PWA's 0.6s ease-out
        label = "progress_animation"
    )

    Box(
        modifier = modifier
            .height(12.dp)
            .fillMaxWidth()
            .clip(CustomShapes.ProgressIndicator)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                .clip(CustomShapes.ProgressIndicator)
                .background(color)
        )
    }
}

/**
 * Progress indicator with label and count
 */
@Composable
fun LabeledProgressIndicator(
    label: String,
    completed: Int,
    total: Int,
    progress: Float,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Text(
                text = "$completed / $total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        PandaProgressIndicator(
            progress = progress,
            color = color
        )
    }
}