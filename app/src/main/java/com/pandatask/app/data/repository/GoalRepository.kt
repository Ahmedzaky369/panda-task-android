package com.pandatask.app.data.repository

import com.pandatask.app.data.database.dao.GoalDao
import com.pandatask.app.data.database.entity.toDomain
import com.pandatask.app.data.database.entity.toEntity
import com.pandatask.app.domain.model.Goal
import com.pandatask.app.domain.model.GoalType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Goal operations
 * Provides clean API for goal data operations
 */
@Singleton
class GoalRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    
    fun getWeeklyGoals(): Flow<List<Goal>> {
        return goalDao.getWeeklyGoals().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getMonthlyGoals(): Flow<List<Goal>> {
        return goalDao.getMonthlyGoals().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getAllGoals(): Flow<List<Goal>> {
        return goalDao.getAllGoals().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getGoalById(id: String): Goal? {
        return goalDao.getGoalById(id)?.toDomain()
    }
    
    suspend fun insertGoal(goal: Goal) {
        goalDao.insertGoal(goal.toEntity())
    }
    
    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal.toEntity())
    }
    
    suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal.toEntity())
    }
    
    suspend fun deleteGoalById(id: String) {
        goalDao.deleteGoalById(id)
    }
    
    suspend fun updateGoalCompletionStatus(goalId: String, completed: Boolean) {
        goalDao.updateGoalCompletionStatus(goalId, completed)
    }
    
    suspend fun updateGoalOrder(goalId: String, order: Int) {
        goalDao.updateGoalOrder(goalId, order)
    }
    
    suspend fun updateGoalText(goalId: String, text: String) {
        goalDao.updateGoalText(goalId, text)
    }
    
    suspend fun addWeeklyGoal(text: String) {
        val goal = Goal(
            id = "w${System.currentTimeMillis()}",
            text = text,
            completed = false,
            type = GoalType.WEEKLY,
            createdAt = System.currentTimeMillis()
        )
        insertGoal(goal)
    }
    
    suspend fun addMonthlyGoal(text: String) {
        val goal = Goal(
            id = "m${System.currentTimeMillis()}",
            text = text,
            completed = false,
            type = GoalType.MONTHLY,
            createdAt = System.currentTimeMillis()
        )
        insertGoal(goal)
    }
    
    suspend fun toggleGoalCompletion(goalId: String) {
        val goal = getGoalById(goalId)
        goal?.let {
            updateGoalCompletionStatus(goalId, !it.completed)
        }
    }
    
    suspend fun reorderWeeklyGoals(goalIds: List<String>) {
        goalIds.forEachIndexed { index, goalId ->
            updateGoalOrder(goalId, index)
        }
    }
    
    suspend fun reorderMonthlyGoals(goalIds: List<String>) {
        goalIds.forEachIndexed { index, goalId ->
            updateGoalOrder(goalId, index)
        }
    }
}