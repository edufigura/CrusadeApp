package com.example.crusadeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.crusadeapp.viewmodel.UserViewModel

// Data class para elementos de la lista
data class CrusadeItem(
    val id: Int,
    val title: String,
    val description: String,
    val progress: Float,
    val isCompleted: Boolean = false
)

@Composable
fun HomeScreen(
    viewModel: UserViewModel,
    onNavigateProfile: () -> Unit,
    onNavigateList: () -> Unit
) {
    val name = viewModel.name.collectAsState().value
    // aca se obtiene el Uri de la foto

    val fotoUri = viewModel.fotoUri.collectAsState().value


    // Datos de ejemplo para las cruzadas
    val crusadeItems = listOf(
        CrusadeItem(
            id = 1,
            title = "Cruzada del Saber",
            description = "Completa 10 lecciones de historia",
            progress = 0.7f
        ),
        CrusadeItem(
            id = 2,
            title = "Cruzada del Valor",
            description = "Participa en 5 debates",
            progress = 0.3f
        ),
        CrusadeItem(
            id = 3,
            title = "Cruzada de la Sabiduría",
            description = "Lee 3 libros recomendados",
            progress = 0.9f
        ),
        CrusadeItem(
            id = 4,
            title = "Cruzada del Honor",
            description = "Ayuda a 3 compañeros",
            progress = 0.5f
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header con bienvenida y botón de perfil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9FA8DA))
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¡Bienvenido, ${if (name.isNotBlank()) name else "Cruzado"}!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Continúa tu aventura del conocimiento",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            // Botón de perfil en esquina superior derecha CON FOTO O ICONO
            IconButton(
                onClick = onNavigateProfile,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Perfil",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Estadísticas rápidas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard(
                title = "Cruzadas",
                value = "4",
                subtitle = "Activas",
                color = Color(0xFF7C83FD)
            )
            StatCard(
                title = "Progreso",
                value = "64%",
                subtitle = "General",
                color = Color(0xFF4CAF50)
            )
            StatCard(
                title = "Logros",
                value = "2",
                subtitle = "Completados",
                color = Color(0xFFFF9800)
            )
        }

        // Ver las lista de unidades
        Button(
            onClick = onNavigateList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text("Ver Unidades Warhammer")
        }

        // Lista de cruzadas
        Text(
            text = "Tus Cruzadas Activas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(crusadeItems) { crusade ->
                CrusadeCard(
                    crusadeItem = crusade,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CrusadeCard(
    crusadeItem: CrusadeItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = crusadeItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Dificultad",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = crusadeItem.description,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de progreso
            LinearProgressIndicator(
                progress = crusadeItem.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF7C83FD),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(crusadeItem.progress * 100).toInt()}% completado",
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}