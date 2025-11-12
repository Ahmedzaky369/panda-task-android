package com.pandatask.app.data.database.dao

import androidx.room.*
import com.pandatask.app.data.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Task operations
 * Provides methods for CRUD operations on tasks table
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE isToday = 1 ORDER BY `order` ASC, createdAt ASC")
    fun getTodayTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY categoryId, `order` ASC, createdAt ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE categoryId = :categoryId ORDER BY `order` ASC, createdAt ASC")
    fun getTasksByCategory(categoryId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)

    @Query("DELETE FROM tasks WHERE categoryId = :categoryId")
    suspend fun deleteTasksByCategory(categoryId: String)

    @Query("UPDATE tasks SET isToday = :isToday WHERE id = :taskId")
    suspend fun updateTaskTodayStatus(taskId: String, isToday: Boolean)

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    suspend fun updateTaskCompletionStatus(taskId: String, completed: Boolean)

    @Query("UPDATE tasks SET `order` = :order WHERE id = :taskId")
    suspend fun updateTaskOrder(taskId: String, order: Int)

    @Query("UPDATE tasks SET text = :text WHERE id = :taskId")
    suspend fun updateTaskText(taskId: String, text: String)
}