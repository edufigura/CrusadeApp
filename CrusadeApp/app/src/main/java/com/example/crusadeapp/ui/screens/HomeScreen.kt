package com.example.crusadeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import coil.request.ImageRequest
import com.example.crusadeapp.viewmodel.UserViewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.crusadeapp.R

// Data class para elementos de la lista
data class CrusadeCardInfo(
    val id: Int,
    val title: String,
    val image: Any
)

data class WeatherInfo(
    val city: String,
    val temp: Double,
    val iconUrl: String
)
// Nuevo diseño para HomeScreen con modo oscuro, cards de cruzadas, botón estilizado,
// clima y GPS. Inserta esto en tu proyecto y reemplaza tu HomeScreen actual.

//----------------------------------------------------
// HOMESCREEN
//----------------------------------------------------

@Composable
fun HomeScreen(
    viewModel: UserViewModel,
    onNavigateProfile: () -> Unit,
    onNavigateList: () -> Unit,
) {
    val name = viewModel.name.collectAsState().value
    val fotoUri = viewModel.fotoUri.collectAsState().value

    val crusades = listOf(
        CrusadeCardInfo(1, "Indomitus Crusade", R.drawable.imagen_1),
        CrusadeCardInfo(2, "Stygian Crusade", R.drawable.imagen_2),
        CrusadeCardInfo(3, "Black Crusade", R.drawable.imagen_3)
    )


    // Datos falsos de clima (conecta tu API luego)
    val weather = WeatherInfo("Santiago", 22.5, "https://openweathermap.org/img/wn/10d@2x.png")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {

        //----------------------------------------------------
        // HEADER
        //----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Bienvenido",
                    fontSize = 14.sp,
                    color = Color(0xFFB0B0B0),
                )
                Text(
                    text = name.ifBlank { "Cruzado" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

            IconButton(onClick = onNavigateProfile) {
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto Perfil",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        }

        //----------------------------------------------------
        // TITULO DE CRUZADAS
        //----------------------------------------------------
        Text(
            text = "Cruzadas en las que estás participando",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        //----------------------------------------------------
        // CARDS DE CRUZADAS
        //----------------------------------------------------
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(crusades.size) { index ->
                CrusadeImageCard(crusades[index])
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        //----------------------------------------------------
        // BOTÓN MEJORADO - UNIDADES WARHAMMER
        //----------------------------------------------------
        Button(
            onClick = onNavigateList,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF313131)),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Ver Unidades de Warhammer",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        //----------------------------------------------------
        // SECCIÓN FINAL – CIUDAD Y CLIMA
        //----------------------------------------------------
        WeatherBottomBox(weather)
    }
}

//----------------------------------------------------
// CARD DE CRUZADA
//----------------------------------------------------
@Composable
fun CrusadeImageCard(data: CrusadeCardInfo) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(220.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        when (val img = data.image) {
            is Int -> Image(
                painter = painterResource(id = img),
                contentDescription = data.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            is String -> AsyncImage(
                model = img,
                contentDescription = data.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

//----------------------------------------------------
// CLIMA Y CIUDAD
//----------------------------------------------------
@Composable
fun WeatherBottomBox(weather: WeatherInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = Color(0xFFB0B0B0)
                    )
                    Text(weather.city, color = Color.White, fontSize = 18.sp)
                }
                Text("${weather.temp}°C", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weather.iconUrl)       // tu URL del icono
                    .crossfade(true)             // animación suave al cargar
                    .build(),
                contentDescription = "Icono clima",
                modifier = Modifier.size(64.dp)
            )

        }
    }
}
