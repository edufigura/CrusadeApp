package com.example.crusadeapp.repository

import android.content.Context
import com.example.crusadeapp.model.Faction
import com.example.crusadeapp.model.UnitData
import com.example.crusadeapp.model.Weapon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListRepository(private val context: Context) {

    suspend fun getFactionData(): Faction = withContext(Dispatchers.IO) {
        // Simulación de carga desde archivo o API
        val sampleWeapons = listOf(
            Weapon("Bolter", "Ranged", range = "24\"", A = "2", BS = "3+", WS = "-", S = 4, AP = -1, D = "1"),
            Weapon("Chainsword", "Melee", A = "4", WS = "3+", S = 4, AP = 0, D = "1")
        )

        val sampleUnits = listOf(
            UnitData(
                name = "Space Marine",
                M = "6\"",
                T = 4,
                SV = "3+",
                W = 2,
                LD = 6,
                OC = 1,
                weapons = sampleWeapons,
                abilities = listOf("And They Shall Know No Fear", "Bolter Discipline")
            ),
            UnitData(
                name = "Terminator",
                M = "5\"",
                T = 5,
                SV = "2+",
                W = 3,
                LD = 6,
                OC = 1,
                weapons = listOf(
                    Weapon("Storm Bolter", "Ranged", range = "24\"", A = "4", BS = "3+", WS = "-", S = 4, AP = 0, D = "1"),
                    Weapon("Power Fist", "Melee", A = "2", WS = "3+", S = 8, AP = -2, D = "2")
                ),
                abilities = listOf("Teleport Strike", "Crux Terminatus")
            )
        )

        Faction(faction = "Space Marines", units = sampleUnits)
    }
}
