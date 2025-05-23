package org.example.project

import android.app.Application
import com.example.reviu_app_jpc.androidAppContext

class Reviu : Application() {
    override fun onCreate() {
        super.onCreate()
        androidAppContext = this // inicialitzar aquí
    }
}
