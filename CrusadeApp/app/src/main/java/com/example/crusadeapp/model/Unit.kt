package com.example.crusadeapp.model

data class Faction(
    val faction: String,
    val units: List<UnitData>
)

//Una unidad tiene sus caracteristicas, habilidades y armas.
data class UnitData(
    val name: String,
    val M: String,
    val T: Int,
    val SV: String,
    val W: Int,
    val LD: Int,
    val OC: Int,
    val weapons: List<Weapon>,
    val abilities: List<String>
)
// Las armas tambien poseen caracteristicas
data class Weapon(
    val name: String,
    val type: String,
    val range: String? = null,
    val A: String,
    val BS: String? = null,
    val WS: String,
    val S: Int,
    val AP: Int,
    val D: String
)
