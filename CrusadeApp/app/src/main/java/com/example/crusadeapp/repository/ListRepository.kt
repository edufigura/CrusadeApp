package com.example.crusadeapp.repository

import android.content.Context
import com.example.crusadeapp.model.Faction
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListRepository(private val context: Context) {

    suspend fun getFactionData(): Faction = withContext(Dispatchers.IO) {
        val inputStream = context.resources.openRawResource(
            com.example.crusadeapp.R.raw.warhammer_units
        )
        val json = inputStream.bufferedReader().use { it.readText() }
        Gson().fromJson(json, Faction::class.java)
    }
}
