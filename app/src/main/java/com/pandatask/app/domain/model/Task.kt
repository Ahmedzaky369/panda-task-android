package com.pandatask.app.domain.model

/**
 * Domain model for a task
 * Mirrors the Task interface from the PWA
 */
data class Task(
    val id: String,
    val text: String,
    val categoryId: String?,
    val completed: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val order: Int = 0
)