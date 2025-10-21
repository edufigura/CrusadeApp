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
    val faction: StateFlow<Faction?> get() = _faction

    fun loadData() {
        viewModelScope.launch {
            try {
                _faction.value = repository.getFactionData()
            } catch (e: Exception) {
                e.printStackTrace()
                _faction.value = null
            }
        }
    }
}
