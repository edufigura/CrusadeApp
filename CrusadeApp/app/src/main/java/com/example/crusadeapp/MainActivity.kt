package com.example.crusadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crusadeapp.repository.UserRepository
import com.example.crusadeapp.ui.screens.*
import com.example.crusadeapp.viewmodel.UserViewModel
import com.example.crusadeapp.viewmodel.UserViewModelFactory
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {

    private val repository by lazy { UserRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(repository))

            NavHost(navController = navController, startDestination = "splash") {

                composable("splash") {
                    SplashScreen()
                    LaunchedEffect(Unit) {
                        delay(2000)
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }

                composable("login") {
                    LoginScreen(
                        viewModel = userViewModel,
                        onNavigateRegister = { navController.navigate("register") },
                        onNavigateHome = { navController.navigate("home") } // Cambiado a home
                    )
                }

                composable("register") {
                    RegisterScreen(
                        viewModel = userViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateHome = { navController.navigate("home") } // Cambiado a home
                    )
                }

                composable("home") {
                    HomeScreen(
                        viewModel = userViewModel,
                        onNavigateProfile = { navController.navigate("profile") },
                        onNavigateList = { navController.navigate("list") }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        viewModel = userViewModel,
                        onBack = { navController.popBackStack() },
                        onLogout = {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }

                composable("list") {
                    // Aquí debes llamar a la función Composable de tu pantalla de lista.
                    // Suponiendo que se llama ListScreen y no necesita parámetros por ahora:
                    ListScreen()
                }
            }
        }
    }
}