package com.example.crusadeapp.repository

import android.content.Context
import com.example.crusadeapp.model.SavedUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ArmyRepository(private val context: Context) {
    private val gson = Gson()
    private val fileName = "saved_units.json"

    private fun getFile(): File = File(context.filesDir, fileName)

    suspend fun getAllSavedUnits(): List<SavedUnit> = withContext(Dispatchers.IO) {
        val file = getFile()
        if (!file.exists()) return@withContext emptyList()
        try {
            val json = file.readText()
            val type = object : TypeToken<List<SavedUnit>>() {}.type
            gson.fromJson<List<SavedUnit>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun saveAll(units: List<SavedUnit>) = withContext(Dispatchers.IO) {
        val file = getFile()
        val json = gson.toJson(units)
        file.writeText(json)
    }

    suspend fun addSavedUnit(unit: SavedUnit) {
        val list = getAllSavedUnits().toMutableList()
        list.add(unit)
        saveAll(list)
    }

    suspend fun updateModifier(instanceId: String, stat: String, value: String) {
        val list = getAllSavedUnits().toMutableList()
        val idx = list.indexOfFirst { it.instanceId == instanceId }
        if (idx >= 0) {
            list[idx].instanceModifiers[stat] = value
            saveAll(list)
        }
    }

    suspend fun removeSavedUnit(instanceId: String) {
        val list = getAllSavedUnits().toMutableList()
        val removed = list.removeAll { it.instanceId == instanceId }
        if (removed) saveAll(list)
    }

    // helper to replace entire list (if necesario)
    suspend fun replaceAll(units: List<SavedUnit>) = saveAll(units)
}
