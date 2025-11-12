package com.pandatask.app.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.pandatask.app.data.database.entity.CategoryEntity
import com.pandatask.app.data.database.entity.TaskEntity
import com.pandatask.app.data.database.entity.GoalEntity

/**
 * Database callback to prepopulate with initial data
 * Mirrors the initial data from the PWA
 */
class DatabaseCallback : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        
        // Prepopulate database with initial data from PWA
        INSTANCE?.let { database ->
            CoroutineScope(Dispatchers.IO).launch {
                populateDatabase(database)
            }
        }
    }
    
    private suspend fun populateDatabase(database: PandaTaskDatabase) {
        val categoryDao = database.categoryDao()
        val taskDao = database.taskDao()
        val goalDao = database.goalDao()
        
        // Initial categories - matching PWA
        val initialCategories = listOf(
            CategoryEntity(
                id = "workout",
                name = "Workout", 
                color = "#50C878",
                order = 0
            ),
            CategoryEntity(
                id = "hygiene",
                name = "Hygiene",
                color = "#66BB6A", 
                order = 1
            ),
            CategoryEntity(
                id = "study", 
                name = "Study",
                color = "#4CAF50",
                order = 2
            ),
            CategoryEntity(
                id = "personal",
                name = "Personal", 
                color = "#81C784",
                order = 3
            )
        )
        
        // Initial tasks - matching PWA
        val initialTasks = listOf(
            TaskEntity(
                id = "1",
                text = "Morning run",
                categoryId = "workout", 
                completed = false,
                createdAt = System.currentTimeMillis(),
                order = 0
            ),
            TaskEntity(
                id = "2", 
                text = "Shower",
                categoryId = "hygiene",
                completed = false,
                createdAt = System.currentTimeMillis(),
                order = 1
            ),
            TaskEntity(
                id = "3",
                text = "Read 20 pages", 
                categoryId = "study",
                completed = false,
                createdAt = System.currentTimeMillis(),
                order = 2
            )
        )
        
        // Initial goals - matching PWA
        val initialGoals = listOf(
            GoalEntity(
                id = "w1",
                text = "Exercise 3 times",
                completed = false,
                type = "WEEKLY",
                createdAt = System.currentTimeMillis(),
                order = 0
            ),
            GoalEntity(
                id = "w2", 
                text = "Meditate daily",
                completed = false,
                type = "WEEKLY",
                createdAt = System.currentTimeMillis(),
                order = 1
            ),
            GoalEntity(
                id = "m1",
                text = "Finish online course", 
                completed = false,
                type = "MONTHLY",
                createdAt = System.currentTimeMillis(),
                order = 0
            )
        )
        
        // Insert initial data
        categoryDao.insertCategories(initialCategories)
        taskDao.insertTasks(initialTasks)
        goalDao.insertGoals(initialGoals)
    }
    
    companion object {
        @Volatile
        private var INSTANCE: PandaTaskDatabase? = null
        
        fun setInstance(instance: PandaTaskDatabase) {
            INSTANCE = instance
        }
    }
}