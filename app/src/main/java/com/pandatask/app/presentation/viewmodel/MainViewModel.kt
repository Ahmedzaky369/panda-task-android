package com.pandatask.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandatask.app.data.preferences.PreferencesManager
import com.pandatask.app.data.repository.CategoryRepository
import com.pandatask.app.data.repository.GoalRepository
import com.pandatask.app.data.repository.TaskRepository
import com.pandatask.app.data.sound.SoundManager
import com.pandatask.app.data.sound.SoundType
import com.pandatask.app.domain.model.Category
import com.pandatask.app.domain.model.Goal
import com.pandatask.app.domain.model.Settings
import com.pandatask.app.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main ViewModel that manages the core app state
 * Mirrors the TaskProvider context from the PWA
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    private val goalRepository: GoalRepository,
    private val preferencesManager: PreferencesManager,
    private val soundManager: SoundManager
) : ViewModel() {

    // Core data streams
    val categories = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allTasks = taskRepository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayTasks = taskRepository.getTodayTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val weeklyGoals = goalRepository.getWeeklyGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val monthlyGoals = goalRepository.getMonthlyGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val settings = preferencesManager.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings()
        )

    val notes = preferencesManager.notes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    // UI state
    private val _selectedTaskIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedTaskIds = _selectedTaskIds.asStateFlow()

    // Task operations
    fun addTask(text: String, categoryId: String?, addToToday: Boolean = false) {
        viewModelScope.launch {
            val task = Task(
                id = System.currentTimeMillis().toString(),
                text = text,
                categoryId = categoryId,
                completed = false,
                createdAt = System.currentTimeMillis()
            )
            taskRepository.insertTask(task, addToToday)
            playSound(SoundType.ADD)
        }
    }

    fun addTaskToTodayOnly(text: String) {
        viewModelScope.launch {
            taskRepository.addTaskToTodayOnly(text)
            playSound(SoundType.ADD)
        }
    }

    fun toggleTaskComplete(taskId: String, isToday: Boolean = false) {
        viewModelScope.launch {
            val task = if (isToday) {
                todayTasks.value.find { it.id == taskId }
            } else {
                allTasks.value.find { it.id == taskId }
            }
            
            task?.let {
                taskRepository.updateTaskCompletionStatus(taskId, !it.completed)
                playSound(SoundType.COMPLETE)
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTaskById(taskId)
            _selectedTaskIds.value = _selectedTaskIds.value - taskId
        }
    }

    fun updateTaskText(taskId: String, text: String) {
        viewModelScope.launch {
            taskRepository.updateTaskText(taskId, text)
        }
    }

    fun addSelectedToToday() {
        viewModelScope.launch {
            val selectedIds = _selectedTaskIds.value.toList()
            taskRepository.addSelectedTasksToToday(selectedIds)
            _selectedTaskIds.value = emptySet()
            playSound(SoundType.ADD)
        }
    }

    fun removeSelectedFromToday() {
        viewModelScope.launch {
            val selectedIds = _selectedTaskIds.value.toList()
            taskRepository.removeSelectedTasksFromToday(selectedIds)
            _selectedTaskIds.value = emptySet()
        }
    }

    fun removeFromToday(taskId: String) {
        viewModelScope.launch {
            taskRepository.updateTaskTodayStatus(taskId, false)
        }
    }

    fun reorderTodayTasks(taskIds: List<String>) {
        viewModelScope.launch {
            taskRepository.reorderTasks(taskIds)
        }
    }

    // Task selection
    fun toggleTaskSelection(taskId: String) {
        _selectedTaskIds.value = if (_selectedTaskIds.value.contains(taskId)) {
            _selectedTaskIds.value - taskId
        } else {
            _selectedTaskIds.value + taskId
        }
    }

    fun clearSelection() {
        _selectedTaskIds.value = emptySet()
    }

    // Category operations
    fun addCategory(name: String, color: String) {
        viewModelScope.launch {
            categoryRepository.addCategory(name, color)
        }
    }

    fun updateCategory(categoryId: String, name: String, color: String) {
        viewModelScope.launch {
            val category = categories.value.find { it.id == categoryId }
            category?.let {
                val updated = it.copy(name = name, color = color)
                categoryRepository.updateCategory(updated)
            }
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategoryById(categoryId)
            taskRepository.deleteTasksByCategory(categoryId)
        }
    }

    fun reorderCategories(categoryIds: List<String>) {
        viewModelScope.launch {
            categoryRepository.reorderCategories(categoryIds)
        }
    }

    // Goal operations
    fun addWeeklyGoal(text: String) {
        viewModelScope.launch {
            goalRepository.addWeeklyGoal(text)
            playSound(SoundType.ADD)
        }
    }

    fun addMonthlyGoal(text: String) {
        viewModelScope.launch {
            goalRepository.addMonthlyGoal(text)
            playSound(SoundType.ADD)
        }
    }

    fun toggleGoalComplete(goalId: String) {
        viewModelScope.launch {
            goalRepository.toggleGoalCompletion(goalId)
            playSound(SoundType.COMPLETE)
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            goalRepository.deleteGoalById(goalId)
        }
    }

    // Settings operations
    fun updateSettings(newSettings: Settings) {
        viewModelScope.launch {
            preferencesManager.updateSettings(newSettings)
        }
    }

    fun updateNotes(notes: String) {
        viewModelScope.launch {
            preferencesManager.updateNotes(notes)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            // Clear database data would require additional repository methods
            preferencesManager.clearAllData()
            _selectedTaskIds.value = emptySet()
        }
    }

    private suspend fun playSound(type: SoundType) {
        soundManager.playSound(type, settings.value.soundEnabled)
    }

    // Helper computed properties
    val weeklyProgress: StateFlow<Float> = weeklyGoals.map { goals ->
        if (goals.isEmpty()) 0f
        else goals.count { it.completed }.toFloat() / goals.size.toFloat()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0f
    )

    val monthlyProgress: StateFlow<Float> = monthlyGoals.map { goals ->
        if (goals.isEmpty()) 0f
        else goals.count { it.completed }.toFloat() / goals.size.toFloat()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0f
    )
}