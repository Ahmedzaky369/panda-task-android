package com.pandatask.app.di

import android.content.Context
import androidx.room.Room
import com.pandatask.app.data.database.PandaTaskDatabase
import com.pandatask.app.data.database.dao.CategoryDao
import com.pandatask.app.data.database.dao.GoalDao
import com.pandatask.app.data.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PandaTaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PandaTaskDatabase::class.java,
            PandaTaskDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideTaskDao(database: PandaTaskDatabase): TaskDao {
        return database.taskDao()
    }
    
    @Provides
    fun provideCategoryDao(database: PandaTaskDatabase): CategoryDao {
        return database.categoryDao()
    }
    
    @Provides
    fun provideGoalDao(database: PandaTaskDatabase): GoalDao {
        return database.goalDao()
    }
}