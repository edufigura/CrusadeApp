package com.example.crusadeapp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.crusadeapp.viewmodel.ListViewModel
import com.example.crusadeapp.repository.ListRepository
import com.example.crusadeapp.model.UnitData

@Composable
fun ListScreen(
    onNavigateHome: () -> Unit
) {
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

                Spacer(modifier = Modifier.height(8.dp))
                BackToHomeButton(onNavigateHome = onNavigateHome)

                Text(
                    text = f.faction,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(f.units.size) { index ->
                        ExpandableUnitCard(
                            unit = f.units[index],
                            viewModel = viewModel
                        )
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando datos...")
        }
    }
}

@Composable
fun ExpandableUnitCard(unit: UnitData, viewModel: ListViewModel) {
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
                Text("Stats", style = MaterialTheme.typography.titleMedium)

                // Fila de stats con modificadores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    unit.modifiers.forEach { (stat, modValue) ->
                        val originalValue = when (stat) {
                            "M" -> unit.M.toString()
                            "T" -> unit.T.toString()
                            "SV" -> unit.SV.toString()
                            "W" -> unit.W.toString()
                            "LD" -> unit.LD.toString()
                            "OC" -> unit.OC.toString()
                            else -> ""
                        }

                        StatItem(
                            label = stat,
                            value = originalValue,
                            modifierValue = modValue
                        ) { newValue ->
                            viewModel.updateModifier(unit.name, stat, newValue)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Weapons
                Text("Weapons:", style = MaterialTheme.typography.titleMedium)
                unit.weapons.forEach { weapon ->
                    Text("- ${weapon.name} (${weapon.type}) S:${weapon.S}, AP:${weapon.AP}, D:${weapon.D}")
                }

                Spacer(Modifier.height(6.dp))

                // Abilities
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(width = 40.dp, height = 32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Text(value)
        }

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


@Composable
fun BackToHomeButton(
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = onNavigateHome) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver al Home",
                tint = Color.Black
            )
        }
    }
}
