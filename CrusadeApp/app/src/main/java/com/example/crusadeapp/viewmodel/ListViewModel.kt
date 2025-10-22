package com.example.crusadeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crusadeapp.model.Faction
import com.example.crusadeapp.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val repository: ListRepository) : ViewModel() {

    private val _faction = MutableStateFlow<Faction?>(null)
    val faction: StateFlow<Faction?> = _faction

    fun loadData() {
        viewModelScope.launch {
            val data = repository.getFactionData() // JSON cargado desde assets/raw
            // Inicializar modificadores para cada unidad
            val unitsWithModifiers = data.units.map { unit ->
                unit.copy(modifiers = mutableMapOf(
                    "M" to "+0", "T" to "+0", "SV" to "+0",
                    "W" to "+0", "LD" to "+0", "OC" to "+0"
                ))
            }
            _faction.value = data.copy(units = unitsWithModifiers)
        }
    }

    fun updateModifier(unitName: String, stat: String, newValue: String) {
        val currentFaction = _faction.value ?: return
        val updatedUnits = currentFaction.units.map { unit ->
            if (unit.name == unitName) {
                unit.apply { modifiers[stat] = newValue }
            } else unit
        }
        _faction.value = currentFaction.copy(units = updatedUnits)
    }
}


