package com.pandatask.app.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Shape system matching PWA design
// PWA uses rounded-xl (12px) and rounded-2xl (16px) extensively
val Shapes = Shapes(
    // Small components like chips, small buttons
    extraSmall = RoundedCornerShape(8.dp),
    
    // Medium components like buttons, input fields  
    small = RoundedCornerShape(12.dp),        // rounded-xl from PWA
    
    // Cards, containers
    medium = RoundedCornerShape(16.dp),       // rounded-2xl from PWA
    
    // Large cards, modals
    large = RoundedCornerShape(20.dp),        // rounded-3xl from PWA (24px)
    
    // Full screen modals, bottom sheets
    extraLarge = RoundedCornerShape(28.dp)
)

// Additional custom shapes for specific components
object CustomShapes {
    val TaskCard = RoundedCornerShape(16.dp)         // Task cards
    val CategoryCard = RoundedCornerShape(16.dp)     // Category containers  
    val GoalCard = RoundedCornerShape(16.dp)         // Goal cards
    val Modal = RoundedCornerShape(24.dp)            // Modal dialogs
    val FloatingActionButton = RoundedCornerShape(16.dp)  // FAB shape
    val ProgressIndicator = RoundedCornerShape(8.dp) // Progress bars
    val Chip = RoundedCornerShape(12.dp)             // Small tags/chips
}