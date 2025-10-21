package com.example.crusadeapp.ui.navigation

import androidx.compose.runtime.Composable
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
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Nueva ruta para la pantalla de unidades
        composable("list") {
            ListScreen()
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