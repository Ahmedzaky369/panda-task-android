package com.pandatask.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pandatask.app.domain.model.Task

/**
 * Room entity for Task
 * Maps to the tasks table in SQLite
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val categoryId: String?,
    val completed: Boolean,
    val createdAt: Long,
    val order: Int,
    val isToday: Boolean = false
)

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        text = text,
        categoryId = categoryId,
        completed = completed,
        createdAt = createdAt,
        order = order
    )
}

fun Task.toEntity(isToday: Boolean = false): TaskEntity {
    return TaskEntity(
        id = id,
        text = text,
        categoryId = categoryId,
        completed = completed,
        createdAt = createdAt,
        order = order,
        isToday = isToday
    )
}