package com.example.aqiapp

import android.app.Application

class App : Application() {
    companion object {
        var app: App? = null
        fun getInstance(): App {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}