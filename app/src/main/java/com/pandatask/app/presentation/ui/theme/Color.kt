package com.pandatask.app.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Brand Colors - matching PWA design
val PandaGreen = Color(0xFF50C878)  // #50C878 - main accent color
val PandaGreenLight = Color(0xFF66BB6A)  // #66BB6A
val PandaGreenDark = Color(0xFF4CAF50)   // #4CAF50
val PandaGreenVariant = Color(0xFF81C784)  // #81C784

// Dark Theme Colors - matching PWA neutral colors
val DarkBackground = Color(0xFF0A0A0A)      // Almost black background
val DarkSurface = Color(0xFF171717)        // neutral-900
val DarkSurfaceVariant = Color(0xFF262626)  // neutral-800
val DarkOutline = Color(0xFF404040)        // neutral-700

// Text Colors
val DarkOnBackground = Color(0xFFFFFFFF)   // White text
val DarkOnSurface = Color(0xFFE5E5E5)      // neutral-200
val DarkOnSurfaceVariant = Color(0xFFA3A3A3)  // neutral-400

// Additional colors for UI elements
val ErrorColor = Color(0xFFDC2626)         // red-600
val ErrorSurface = Color(0x33DC2626)       // red-600 with 20% opacity
val SuccessColor = PandaGreen
val WarningColor = Color(0xFFF59E0B)       // amber-500

// Floating Action Button colors
val FabBackground = PandaGreen
val FabContent = Color.White

// Progress bar colors
val ProgressBackground = DarkSurfaceVariant
val ProgressIndicator = PandaGreen

// Selection colors
val SelectionBackground = Color(0x1A50C878)  // PandaGreen with 10% opacity
val SelectionBorder = Color(0x8050C878)     // PandaGreen with 50% opacity