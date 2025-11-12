package com.pandatask.app.data.database.dao

import androidx.room.*
import com.pandatask.app.data.database.entity.GoalEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Goal operations
 * Provides methods for CRUD operations on goals table
 */
@Dao
interface GoalDao {

    @Query("SELECT * FROM goals WHERE type = 'WEEKLY' ORDER BY `order` ASC, createdAt ASC")
    fun getWeeklyGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE type = 'MONTHLY' ORDER BY `order` ASC, createdAt ASC")
    fun getMonthlyGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals ORDER BY type, `order` ASC, createdAt ASC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalById(id: String): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<GoalEntity>)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoalById(id: String)

    @Query("UPDATE goals SET completed = :completed WHERE id = :goalId")
    suspend fun updateGoalCompletionStatus(goalId: String, completed: Boolean)

    @Query("UPDATE goals SET `order` = :order WHERE id = :goalId")
    suspend fun updateGoalOrder(goalId: String, order: Int)

    @Query("UPDATE goals SET text = :text WHERE id = :goalId")
    suspend fun updateGoalText(goalId: String, text: String)
}