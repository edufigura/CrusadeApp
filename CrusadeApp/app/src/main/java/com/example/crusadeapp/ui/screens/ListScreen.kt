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
import com.example.crusadeapp.repository.ArmyRepository
import com.example.crusadeapp.viewmodel.ArmyViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.TextFieldDefaults

@Composable
fun ListScreen(
    onNavigateHome: () -> Unit,
    onNavigateToArmy: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { ListRepository(context) }
    val viewModel = remember { ListViewModel(repository) }

    val armyRepo = remember { ArmyRepository(context) }
    val armyVm = remember { ArmyViewModel(armyRepo) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val faction by viewModel.faction.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212) // Fondo completamente oscuro
    ) {
        faction?.let { f ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                BackToHomeButton(
                    onNavigateHome = onNavigateHome,
                    modifier = Modifier
                )

                Text(
                    text = f.faction,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        itemsIndexed(f.units) { _, unit ->
                            ExpandableUnitCard(
                                unit = unit,
                                viewModel = viewModel,
                                onAddInstance = { armyVm.addUnitInstanceFrom(it) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNavigateToArmy,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Ir a ArmyScreen",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Cargando datos...",
                color = Color.White
            )
        }
    }
}

@Composable
fun ExpandableUnitCard(unit: UnitData, viewModel: ListViewModel, onAddInstance: (UnitData) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E) // Card oscuro
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = unit.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text("Stats", style = MaterialTheme.typography.titleMedium, color = Color.White)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    unit.modifiers.forEach { (stat, modValue) ->
                        val originalValue = when (stat) {
                            "M" -> unit.M
                            "T" -> unit.T.toString()
                            "SV" -> unit.SV
                            "W" -> unit.W.toString()
                            "LD" -> unit.LD.toString()
                            "OC" -> unit.OC.toString()
                            else -> ""
                        }
                        StatItem(stat, originalValue, modValue) { newValue ->
                            viewModel.updateModifier(unit.name, stat, newValue)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text("Weapons:", style = MaterialTheme.typography.titleMedium, color = Color.White)
                unit.weapons.forEach { weapon ->
                    Text(
                        "- ${weapon.name} (${weapon.type}) A:${weapon.A}, S:${weapon.S}, AP:${weapon.AP}, D:${weapon.D}",
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(6.dp))
                Text("Abilities:", style = MaterialTheme.typography.titleMedium, color = Color.White)
                unit.abilities.forEach { ability ->
                    Text("- $ability", color = Color.White)
                }

                Spacer(Modifier.height(6.dp))

                Button(
                    onClick = { onAddInstance(unit) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Añadir a mi lista", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}


@Composable
fun StatItem(
    label: String,
    value: String,
    modifierValue: String,
    onModifierChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Nombre de la característica
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.White)

        // Caja con el valor de la característica
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(width = 40.dp, height = 32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Caja del modificador
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.padding(top = 2.dp).clickable { showDialog = true }
        ) {
            Text(
                modifierValue,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
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
                }) {
                    Text("OK", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            title = { Text("Modificar $label", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Nuevo valor", color = Color.White) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            containerColor = Color(0xFF1E1E1E)
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
        IconButton(
            onClick = onNavigateHome,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver al Home"
            )
        }
    }
}
