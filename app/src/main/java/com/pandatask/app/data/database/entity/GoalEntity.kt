package com.pandatask.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pandatask.app.domain.model.Goal
import com.pandatask.app.domain.model.GoalType

/**
 * Room entity for Goal
 * Maps to the goals table in SQLite
 */
@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val completed: Boolean,
    val type: String, // "WEEKLY" or "MONTHLY"
    val createdAt: Long,
    val order: Int
)

fun GoalEntity.toDomain(): Goal {
    return Goal(
        id = id,
        text = text,
        completed = completed,
        type = GoalType.valueOf(type),
        createdAt = createdAt,
        order = order
    )
}

fun Goal.toEntity(): GoalEntity {
    return GoalEntity(
        id = id,
        text = text,
        completed = completed,
        type = type.name,
        createdAt = createdAt,
        order = order
    )
}