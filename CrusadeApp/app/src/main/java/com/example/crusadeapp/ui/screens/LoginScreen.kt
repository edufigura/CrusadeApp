package com.example.crusadeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crusadeapp.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.crusadeapp.R

@Composable
fun LoginScreen(
    viewModel: UserViewModel? = null,
    onNavigateRegister: () -> Unit = {},
    onNavigateHome: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoggedIn by viewModel?.isLoggedIn?.collectAsState(initial = false)
        ?: remember { mutableStateOf(false) }

    val errorMessage by viewModel?.errorMessage?.collectAsState(initial = null)
        ?: remember { mutableStateOf(null) }

    // Cuadrado con los items
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(id = R.drawable.background_warhammer),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Capa oscura para mejorar contraste
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
        )

        // Tarjeta principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .background(
                    Color(0xAA1C1C1E),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text = "Crusade Manager",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.crusade),
                contentDescription = "Logo",
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Iniciar Sesión",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFB0BEC5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("ej: tuEmail@dominio.cl") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33FFFFFF),
                    unfocusedContainerColor = Color(0x22FFFFFF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFB0BEC5),
                    unfocusedLabelColor = Color(0xFFB0BEC5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("ej: tuContraseña123") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33FFFFFF),
                    unfocusedContainerColor = Color(0x22FFFFFF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFB0BEC5),
                    unfocusedLabelColor = Color(0xFFB0BEC5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Login (gradiente)
            Button(
                onClick = { viewModel?.login(email, password) },
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF5C6BC0),
                                    Color(0xFF3949AB)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Iniciar Sesión", color = Color.White)
                }
            }

            if (isLoggedIn) {
                LaunchedEffect(true) { onNavigateHome() }
            }

            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Registrarse
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = Color(0xFF90CAF9),
                modifier = Modifier.clickable { onNavigateRegister() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Olvidaste contraseña
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF80DEEA),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}

