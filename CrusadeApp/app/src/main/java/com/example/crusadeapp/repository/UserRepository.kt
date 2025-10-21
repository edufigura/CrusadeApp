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
    private val fotokey = stringPreferencesKey("foto")

    //Funcion para guardar el usuario

    suspend fun saveUser(name: String, email: String, fotoUri: String?){
        context.dataStore.edit{ prefs ->
            prefs[nameKey] = name
            prefs[emailKey] = email
            prefs[fotokey] = fotoUri ?: ""
        }
    }

    //Funcion para Cargar el usuario
    suspend fun getUser(): Triple<String, String, String?>{
        val prefs = context.dataStore.data.first()
        val name = prefs[nameKey] ?: ""
        val email = prefs[emailKey] ?: ""
        val foto = prefs[fotokey]?.takeIf { it.isNotEmpty() }
        return Triple(name, email, foto)
    }

}