package com.example.crusadeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crusadeapp.viewmodel.ArmyViewModel
import androidx.compose.ui.graphics.Color

@Composable
fun ArmyScreen(
    armyViewModel: ArmyViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToList: () -> Unit
) {
    val saved by armyViewModel.savedUnits.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212) // Fondo oscuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            BackToHomeButton(onNavigateHome = onNavigateBack)

            Text(
                "Mi lista",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (saved.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay unidades en tu lista",
                            color = Color.White
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(saved.size) { idx ->
                            val inst = saved[idx]
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1E1E1E) // Card oscuro
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            inst.baseName,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White
                                        )
                                        IconButton(onClick = { armyViewModel.removeInstance(inst.instanceId) }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.Red
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        inst.instanceModifiers.forEach { (stat, modValue) ->
                                            StatItem(
                                                label = stat,
                                                value = when(stat) {
                                                    "M" -> inst.M
                                                    "T" -> inst.T.toString()
                                                    "SV" -> inst.SV
                                                    "W" -> inst.W.toString()
                                                    "LD" -> inst.LD.toString()
                                                    "OC" -> inst.OC.toString()
                                                    else -> ""
                                                },
                                                modifierValue = modValue
                                            ) { newValue ->
                                                armyViewModel.updateModifier(inst.instanceId, stat, newValue)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para navegar a ListScreen
            Button(
                onClick = onNavigateToList,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Ir a ListScreen",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

