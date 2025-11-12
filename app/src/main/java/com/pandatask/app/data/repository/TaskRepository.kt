package com.pandatask.app.data.repository

import com.pandatask.app.data.database.dao.TaskDao
import com.pandatask.app.data.database.entity.toDomain
import com.pandatask.app.data.database.entity.toEntity
import com.pandatask.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Task operations
 * Provides clean API for task data operations, abstracts database implementation
 */
@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    
    fun getTodayTasks(): Flow<List<Task>> {
        return taskDao.getTodayTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getTasksByCategory(categoryId: String): Flow<List<Task>> {
        return taskDao.getTasksByCategory(categoryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getTaskById(id: String): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }
    
    suspend fun insertTask(task: Task, addToToday: Boolean = false) {
        taskDao.insertTask(task.toEntity(isToday = addToToday))
    }
    
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }
    
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
    
    suspend fun deleteTaskById(id: String) {
        taskDao.deleteTaskById(id)
    }
    
    suspend fun deleteTasksByCategory(categoryId: String) {
        taskDao.deleteTasksByCategory(categoryId)
    }
    
    suspend fun updateTaskTodayStatus(taskId: String, isToday: Boolean) {
        taskDao.updateTaskTodayStatus(taskId, isToday)
    }
    
    suspend fun updateTaskCompletionStatus(taskId: String, completed: Boolean) {
        taskDao.updateTaskCompletionStatus(taskId, completed)
    }
    
    suspend fun updateTaskOrder(taskId: String, order: Int) {
        taskDao.updateTaskOrder(taskId, order)
    }
    
    suspend fun updateTaskText(taskId: String, text: String) {
        taskDao.updateTaskText(taskId, text)
    }
    
    suspend fun addTaskToTodayOnly(text: String) {
        val task = Task(
            id = System.currentTimeMillis().toString(),
            text = text,
            categoryId = null,
            completed = false,
            createdAt = System.currentTimeMillis()
        )
        insertTask(task, addToToday = true)
    }
    
    suspend fun addSelectedTasksToToday(taskIds: List<String>) {
        taskIds.forEach { taskId ->
            updateTaskTodayStatus(taskId, true)
        }
    }
    
    suspend fun removeSelectedTasksFromToday(taskIds: List<String>) {
        taskIds.forEach { taskId ->
            updateTaskTodayStatus(taskId, false)
        }
    }
    
    suspend fun reorderTasks(taskIds: List<String>) {
        taskIds.forEachIndexed { index, taskId ->
            updateTaskOrder(taskId, index)
        }
    }
}