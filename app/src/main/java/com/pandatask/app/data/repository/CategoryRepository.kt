package com.pandatask.app.data.repository

import com.pandatask.app.data.database.dao.CategoryDao
import com.pandatask.app.data.database.entity.toDomain
import com.pandatask.app.data.database.entity.toEntity
import com.pandatask.app.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Category operations
 * Provides clean API for category data operations
 */
@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    
    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getCategoryById(id: String): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }
    
    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity())
    }
    
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }
    
    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }
    
    suspend fun deleteCategoryById(id: String) {
        categoryDao.deleteCategoryById(id)
    }
    
    suspend fun updateCategoryOrder(categoryId: String, order: Int) {
        categoryDao.updateCategoryOrder(categoryId, order)
    }
    
    suspend fun updateCategoryName(categoryId: String, name: String) {
        categoryDao.updateCategoryName(categoryId, name)
    }
    
    suspend fun updateCategoryColor(categoryId: String, color: String) {
        categoryDao.updateCategoryColor(categoryId, color)
    }
    
    suspend fun addCategory(name: String, color: String) {
        val category = Category(
            id = System.currentTimeMillis().toString(),
            name = name,
            color = color,
            order = 0
        )
        insertCategory(category)
    }
    
    suspend fun reorderCategories(categoryIds: List<String>) {
        categoryIds.forEachIndexed { index, categoryId ->
            updateCategoryOrder(categoryId, index)
        }
    }
}