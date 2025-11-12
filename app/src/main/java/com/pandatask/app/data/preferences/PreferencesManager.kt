package com.pandatask.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pandatask.app.domain.model.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages app preferences using DataStore
 * Stores settings and notes data persistently
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "panda_task_preferences")
        
        private val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val THEME = stringPreferencesKey("theme")
        private val ACCENT_COLOR = stringPreferencesKey("accent_color")
        private val NOTES = stringPreferencesKey("notes")
    }
    
    val settings: Flow<Settings> = context.dataStore.data.map { preferences ->
        Settings(
            soundEnabled = preferences[SOUND_ENABLED] ?: true,
            theme = preferences[THEME] ?: "dark",
            accentColor = preferences[ACCENT_COLOR] ?: "#50C878"
        )
    }
    
    val notes: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NOTES] ?: ""
    }
    
    suspend fun updateSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SOUND_ENABLED] = enabled
        }
    }
    
    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME] = theme
        }
    }
    
    suspend fun updateAccentColor(color: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = color
        }
    }
    
    suspend fun updateSettings(settings: Settings) {
        context.dataStore.edit { preferences ->
            preferences[SOUND_ENABLED] = settings.soundEnabled
            preferences[THEME] = settings.theme
            preferences[ACCENT_COLOR] = settings.accentColor
        }
    }
    
    suspend fun updateNotes(notes: String) {
        context.dataStore.edit { preferences ->
            preferences[NOTES] = notes
        }
    }
    
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}