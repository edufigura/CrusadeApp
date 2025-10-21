package com.example.crusadeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crusadeapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository) : ViewModel(){
//MutableStateFlow es un flujo de datos que podria cambiar con el tiempo. Idealmente para mantener estados en compose
    val name  = MutableStateFlow("")
    val email = MutableStateFlow("")
    val fotoUri = MutableStateFlow<String?>(null)


    //Se activa una corrutina y se guarda el usuario en Localstorage
    fun register(name: String, email: String){
        viewModelScope.launch {
            repository.saveUser(name, email, null)
            this@UserViewModel.name.value = name
            this@UserViewModel.email.value = email
        }
    }

    //Se guarda el usuario logeado similar a register
    fun login(name: String, email: String){
        viewModelScope.launch {
            repository.saveUser(name,email,null)
            this@UserViewModel.name.value = name
            this@UserViewModel.email.value = email
        }
    }

    fun actualizarFoto(uri: String){
        viewModelScope.launch {
            repository.saveUser(name.value, email.value, uri)
            fotoUri.value = uri
        }
    }


}