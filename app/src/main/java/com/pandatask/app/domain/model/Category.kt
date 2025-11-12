package com.pandatask.app.domain.model

/**
 * Domain model for a task category
 * Mirrors the Category interface from the PWA
 */
data class Category(
    val id: String,
    val name: String,
    val color: String,
    val order: Int = 0
)