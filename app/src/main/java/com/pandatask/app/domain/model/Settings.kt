package com.pandatask.app.domain.model

/**
 * Domain model for app settings
 * Mirrors the Settings interface from the PWA
 */
data class Settings(
    val soundEnabled: Boolean = true,
    val theme: String = "dark",
    val accentColor: String = "#50C878"
)