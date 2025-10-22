package com.example.crusadeapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crusadeapp.ui.screens.*
import com.example.crusadeapp.viewmodel.UserViewModel

@Composable
fun AppNavigation(navController: NavHostController, userViewModel: UserViewModel){
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = userViewModel,
                onNavigateRegister = { navController.navigate("register") },
                onNavigateHome = { navController.navigate("home") }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = userViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateHome = { navController.navigate("home") }
            )
        }

        composable("profile") {
            ProfileScreen(
                viewModel = userViewModel,
                onLogout = {
                    userViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateHome = { navController.navigate("home") }
            )
        }


        // Nueva ruta para la pantalla de unidades
        composable("list") {
            ListScreen(
                onNavigateHome = { navController.navigate("home") }
            )

        }

        composable("home") {
            HomeScreen(
                viewModel = userViewModel,
                onNavigateProfile = { navController.navigate("profile") },
                onNavigateList = { navController.navigate("list") }
            )
        }

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