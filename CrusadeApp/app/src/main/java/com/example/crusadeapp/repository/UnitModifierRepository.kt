package com.example.crusadeapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "modifiers")

class UnitModifierRepository(private val context: Context) {

    fun getModifier(unitName: String, stat: String): Flow<String> {
        val key = stringPreferencesKey("${unitName}_$stat")
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: "+0"
        }
    }

    suspend fun saveModifier(unitName: String, stat: String, value: String) {
        val key = stringPreferencesKey("${unitName}_$stat")
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
}
