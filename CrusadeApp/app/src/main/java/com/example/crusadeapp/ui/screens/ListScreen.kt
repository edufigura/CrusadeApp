package com.example.crusadeapp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.crusadeapp.viewmodel.ListViewModel
import com.example.crusadeapp.repository.ListRepository
import com.example.crusadeapp.model.UnitData

@Composable
fun ListScreen() {
    val context = LocalContext.current
    val repository = remember { ListRepository(context) }
    val viewModel = remember { ListViewModel(repository) }

    // Cargar datos al inicio
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val faction by viewModel.faction.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        faction?.let { f ->
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = f.faction,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(f.units.size) { index ->
                        ExpandableUnitCard(f.units[index])
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Cargando datos...")
        }
    }
}

@Composable
fun ExpandableUnitCard(unit: UnitData) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = unit.name, style = MaterialTheme.typography.titleLarge)
            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text("M: ${unit.M}  T: ${unit.T}  SV: ${unit.SV}")
                Text("W: ${unit.W}  LD: ${unit.LD}  OC: ${unit.OC}")
                Spacer(Modifier.height(6.dp))

                // Armas
                Text("Weapons:", style = MaterialTheme.typography.titleMedium)
                unit.weapons.forEach { weapon ->
                    Text("- ${weapon.name} (${weapon.type}) " +
                            "S:${weapon.S}, AP:${weapon.AP}, D:${weapon.D}")
                }

                Spacer(Modifier.height(6.dp))

                // Habilidades
                Text("Abilities:", style = MaterialTheme.typography.titleMedium)
                unit.abilities.forEach { ability ->
                    Text("- $ability")
                }
            }
        }
    }
}
