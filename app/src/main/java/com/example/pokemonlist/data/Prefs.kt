package com.example.pokemonlist.data

import android.content.Context

class Prefs(context: Context) {
    val SHARED_NAME = "Mydb"
    val SHARED_CORREO = "email"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveEmail(email: String) = storage.edit().putString(SHARED_CORREO, email).apply()

    fun getEmail(): String = storage.getString(SHARED_CORREO, "")!!

    fun wipe(){
        storage.edit().clear().apply()
    }
}