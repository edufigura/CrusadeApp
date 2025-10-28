package com.example.crusadeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crusadeapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository) : ViewModel(){


//MutableStateFlow es un flujo de datos que podria cambiar con el tiempo. Idealmente para mantener estados en compose
    val name  = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    //Estado privado para la foto del usuario.
    private val _fotoUri = MutableStateFlow<String?>(null)
    val fotoUri = _fotoUri.asStateFlow()

    val isLoggedIn = MutableStateFlow(false)
    val errorMessage = MutableStateFlow<String?>(null)

    //Se activa una corrutina y se guarda el usuario en Localstorage
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.saveUser(name, email, password, null)
            this@UserViewModel.name.value = name
            this@UserViewModel.email.value = email
            this@UserViewModel.password.value = password
            isLoggedIn.value = false
        }
    }

    //Se guarda el usuario logeado similar a register
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val storedUser = repository.getUser()
            if (email == storedUser.second && password == storedUser.third) {
                this@UserViewModel.name.value = storedUser.first
                this@UserViewModel.email.value = storedUser.second
                this@UserViewModel.password.value = storedUser.third
                _fotoUri.value = storedUser.fourth
                isLoggedIn.value = true
                errorMessage.value = null
            } else {
                errorMessage.value = "Email o contraseña incorrectos"
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            val user = repository.getUser()
            this@UserViewModel.name.value = user.first
            this@UserViewModel.email.value = user.second
            _fotoUri.value = user.fourth
            isLoggedIn.value = user.second.isNotEmpty()
        }
    }

    fun actualizarFoto(uri: String) {
        viewModelScope.launch {
            repository.saveUser(name.value, email.value, "", uri)
            _fotoUri.value = uri
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.saveUser("", "", "", null)
            name.value = ""
            email.value = ""
            password.value = ""
            _fotoUri.value = null
            isLoggedIn.value = false
        }
    }
}