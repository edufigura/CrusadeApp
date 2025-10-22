package com.example.crusadeapp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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

    // Mapa de modificadores (uno por stat)
    var modifiers by remember {
        mutableStateOf(
            mutableMapOf(
                "M" to "+0", "T" to "+0", "SV" to "+0",
                "W" to "+0", "LD" to "+0", "OC" to "+0"
            )
        )
    }

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

                Text("Stats", style = MaterialTheme.typography.titleMedium)

                // Fila de stats con modificadores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("M", unit.M, modifiers["M"] ?: "+0") { newValue ->
                        modifiers["M"] = newValue
                    }
                    StatItem("T", unit.T.toString(), modifiers["T"] ?: "+0") { newValue ->
                        modifiers["T"] = newValue
                    }
                    StatItem("SV", unit.SV, modifiers["SV"] ?: "+0") { newValue ->
                        modifiers["SV"] = newValue
                    }
                    StatItem("W", unit.W.toString(), modifiers["W"] ?: "+0") { newValue ->
                        modifiers["W"] = newValue
                    }
                    StatItem("LD", unit.LD.toString(), modifiers["LD"] ?: "+0") { newValue ->
                        modifiers["LD"] = newValue
                    }
                    StatItem("OC", unit.OC.toString(), modifiers["OC"] ?: "+0") { newValue ->
                        modifiers["OC"] = newValue
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text("Weapons:", style = MaterialTheme.typography.titleMedium)
                unit.weapons.forEach { weapon ->
                    Text("- ${weapon.name} (${weapon.type}) S:${weapon.S}, AP:${weapon.AP}, D:${weapon.D}")
                }

                Spacer(Modifier.height(6.dp))

                Text("Abilities:", style = MaterialTheme.typography.titleMedium)
                unit.abilities.forEach { ability ->
                    Text("- $ability")
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifierValue: String, onModifierChange: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(width = 40.dp, height = 32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(value)
        }

        // Círculo del modificador
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .padding(top = 2.dp)
                .clickable { showDialog = true }
        ) {
            Text(
                modifierValue,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }

    // Diálogo para editar modificador
    if (showDialog) {
        var inputValue by remember { mutableStateOf(modifierValue) }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onModifierChange(inputValue)
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
            title = { Text("Modificar $label") },
            text = {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Nuevo valor") }
                )
            }
        )
    }
}
