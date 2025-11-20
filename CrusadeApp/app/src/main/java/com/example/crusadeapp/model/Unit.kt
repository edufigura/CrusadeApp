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
    val abilities: List<String>,
    val modifiers: MutableMap<String, String> = mutableMapOf(
        "M" to "+0", "T" to "+0", "SV" to "+0", "W" to "+0", "LD" to "+0", "OC" to "+0"
    )
)
// Las armas tambien poseen caracteristicas
data class Weapon(
    val name: String,
    val type: String,
    val range: String? = null,
    val A: Any,
    val BS: String? = null,
    val WS: String,
    val S: Int,
    val AP: Int,
    val D: Any
) {
    val attacksText: String get() = A.toString()
    val damageText: String get() = D.toString()
}
