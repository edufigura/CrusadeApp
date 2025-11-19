package com.example.crusadeapp.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.crusadeapp.viewmodel.UserViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onLogout: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val name = viewModel.name.collectAsState().value
    val email = viewModel.email.collectAsState().value
    val fotoUri = viewModel.fotoUri.collectAsState().value

    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // Crear archivo temporal para la foto
    fun createImageUri(context: Context): Uri? {
        val imageFile = File(context.cacheDir, "temp_photo.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }

    // Lanzador de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.actualizarFoto(it.toString())
        }
    }

    // Lanzador de cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraUri != null) {
            viewModel.actualizarFoto(cameraUri.toString())
        }
    }

    // Permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraUri = createImageUri(context)
            cameraUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar opción") },
            text = { Text("¿Deseas tomar una foto o elegir desde galería?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }) { Text("Tomar foto") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    galleryLauncher.launch("image/*")
                }) { Text("Galería") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        BackToHomeButton(onNavigateHome = onNavigateHome)

        // Título
        Text(
            text = "Perfil de Usuario",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Caja de perfil con fondo más claro
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            if (!fotoUri.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color(0xFFB0B0B0),
                    modifier = Modifier.size(80.dp)
                )
            }

            // Botón flotante para cambiar foto
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(44.dp)
                    .background(Color(0xFF2C2C2C), CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Cambiar foto",
                    tint = Color(0xFF7C83FD)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Datos del usuario
        Text(
            text = "Nombre: ${if (name.isNotBlank()) name else "(Desconocido)"}",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Correo: ${if (email.isNotBlank()) email else "(Desconocido)"}",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Botón de cerrar sesión
        Button(
            onClick = {
                viewModel.register("", "", "")
                onLogout()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C83FD)
            )
        ) {
            Text("Cerrar sesión", color = Color.White, fontSize = 16.sp)
        }
    }
}


