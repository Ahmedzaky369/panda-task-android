package com.pandatask.app.domain.model

/**
 * Domain model for goals (both weekly and monthly)
 * Mirrors the Goal interface from the PWA
 */
data class Goal(
    val id: String,
    val text: String,
    val completed: Boolean,
    val type: GoalType,
    val createdAt: Long = System.currentTimeMillis(),
    val order: Int = 0
)

enum class GoalType {
    WEEKLY,
    MONTHLY
}