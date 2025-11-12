package com.pandatask.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.pandatask.app.data.database.dao.CategoryDao
import com.pandatask.app.data.database.dao.GoalDao
import com.pandatask.app.data.database.dao.TaskDao
import com.pandatask.app.data.database.entity.CategoryEntity
import com.pandatask.app.data.database.entity.GoalEntity
import com.pandatask.app.data.database.entity.TaskEntity

/**
 * Room database for Panda Task app
 * Manages all local data storage
 */
@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        GoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PandaTaskDatabase : RoomDatabase() {
    
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun goalDao(): GoalDao
    
    companion object {
        const val DATABASE_NAME = "panda_task_database"
        
        @Volatile
        private var INSTANCE: PandaTaskDatabase? = null
        
        fun getDatabase(context: Context): PandaTaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PandaTaskDatabase::class.java,
                    DATABASE_NAME
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}