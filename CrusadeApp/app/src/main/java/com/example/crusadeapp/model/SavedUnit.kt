package com.example.crusadeapp.model

import java.util.UUID

data class SavedUnit(
    val instanceId: String = UUID.randomUUID().toString(),
    val baseName: String,
    val M: String,
    val T: Int,
    val SV: String,
    val W: Int,
    val LD: Int,
    val OC: Int,
    val weapons: List<Weapon> = emptyList(),
    val abilities: List<String> = emptyList(),
    val instanceModifiers: MutableMap<String, String> = mutableMapOf(
        "M" to "+0", "T" to "+0", "SV" to "+0", "W" to "+0", "LD" to "+0", "OC" to "+0"
    )
)
