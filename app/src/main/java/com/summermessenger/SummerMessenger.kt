package com.summermessenger

import android.app.Application

class SummerMessenger : Application() {
    companion object {
        lateinit var instance: SummerMessenger
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getResourceString(resId: Int): String {
        return applicationContext.getString(resId)
    }
}