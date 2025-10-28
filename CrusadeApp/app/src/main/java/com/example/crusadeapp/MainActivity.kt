package com.example.crusadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.crusadeapp.repository.UserRepository
import com.example.crusadeapp.viewmodel.UserViewModel
import com.example.crusadeapp.viewmodel.UserViewModelFactory
import com.example.crusadeapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    private val repository by lazy { UserRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(repository))

            AppNavigation(navController = navController, userViewModel = userViewModel)
        }
    }
}