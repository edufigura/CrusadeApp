package com.example.crusadeapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


//Extension para cargar Contexto de dataStore
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserRepository (private val context: Context){

    private val nameKey = stringPreferencesKey("name")
    private val emailKey = stringPreferencesKey("email")
    private val passwordKey = stringPreferencesKey("password")
    private val fotokey = stringPreferencesKey("foto")

    //Funcion para guardar el usuario // Se guarda la contraseña no es seguro

    suspend fun saveUser(name: String, email: String, password: String, fotoUri: String?){
        context.dataStore.edit{ prefs ->
            prefs[nameKey] = name
            prefs[emailKey] = email
            prefs[passwordKey] = password
            prefs[fotokey] = fotoUri ?: ""
        }
    }

    //Funcion para Cargar el usuario
    suspend fun getUser(): Quadruple<String, String, String, String?> {
        val prefs = context.dataStore.data.first()
        val name = prefs[nameKey] ?: ""
        val email = prefs[emailKey] ?: ""
        val password = prefs[passwordKey] ?: ""
        val foto = prefs[fotokey]?.takeIf { it.isNotEmpty() }
        return Quadruple(name, email, password, foto)
    }

    // Verificar credenciales al iniciar sesión // LOGIIIIIN
    suspend fun checkCredentials(email: String, password: String): Boolean {
        val prefs = context.dataStore.data.first()
        val storedEmail = prefs[emailKey] ?: ""
        val storedPassword = prefs[passwordKey] ?: ""
        return email == storedEmail && password == storedPassword
    }
}
// Lista de 4 valores
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)