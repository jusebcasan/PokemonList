package com.example.pokemonlist.data

import android.app.Application

class UserApplicationShared: Application() {

    companion object{
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}