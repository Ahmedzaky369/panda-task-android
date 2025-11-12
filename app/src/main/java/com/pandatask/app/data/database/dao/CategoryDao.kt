package com.pandatask.app.data.database.dao

import androidx.room.*
import com.pandatask.app.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Category operations
 * Provides methods for CRUD operations on categories table
 */
@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY `order` ASC, name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: String)

    @Query("UPDATE categories SET `order` = :order WHERE id = :categoryId")
    suspend fun updateCategoryOrder(categoryId: String, order: Int)

    @Query("UPDATE categories SET name = :name WHERE id = :categoryId")
    suspend fun updateCategoryName(categoryId: String, name: String)

    @Query("UPDATE categories SET color = :color WHERE id = :categoryId")
    suspend fun updateCategoryColor(categoryId: String, color: String)
}