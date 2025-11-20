// file: com.example.crusadeapp.viewmodel/ArmyViewModel.kt
package com.example.crusadeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crusadeapp.model.SavedUnit
import com.example.crusadeapp.model.UnitData
import com.example.crusadeapp.repository.ArmyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArmyViewModel(private val repo: ArmyRepository) : ViewModel() {

    private val _savedUnits = MutableStateFlow<List<SavedUnit>>(emptyList())
    val savedUnits: StateFlow<List<SavedUnit>> = _savedUnits

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val list = repo.getAllSavedUnits()
            _savedUnits.value = list
        }
    }

    fun addUnitInstanceFrom(unit: UnitData) {
        viewModelScope.launch {
            val savedUnit = SavedUnit(
                baseName = unit.name,
                M = unit.M,
                T = unit.T,
                SV = unit.SV,
                W = unit.W,
                LD = unit.LD,
                OC = unit.OC,
                weapons = unit.weapons,
                abilities = unit.abilities,
                // instanceModifiers default already set in constructor
            )
            repo.addSavedUnit(savedUnit)
            refresh()
        }
    }

    fun updateModifier(instanceId: String, stat: String, value: String) {
        viewModelScope.launch {
            repo.updateModifier(instanceId, stat, value)
            refresh()
        }
    }

    fun removeInstance(instanceId: String) {
        viewModelScope.launch {
            repo.removeSavedUnit(instanceId)
            refresh()
        }
    }
}
