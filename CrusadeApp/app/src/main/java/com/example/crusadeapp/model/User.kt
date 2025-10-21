package com.example.crusadeapp.model

//Clase que representara a un usuario registrado
data class User (
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val profileImg: String? = null
)