package com.pandatask.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pandatask.app.domain.model.Category

/**
 * Room entity for Category
 * Maps to the categories table in SQLite
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: String,
    val order: Int
)

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        color = color,
        order = order
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        color = color,
        order = order
    )
}