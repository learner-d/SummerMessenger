package com.summermessenger

import android.app.Application
import android.content.Context

class SummerMessenger : Application() {
    companion object {
//        @SuppressLint("StaticFieldLeak")
        private lateinit var _context: Context
        val context: Context
            get() = _context
    }

    override fun onCreate() {
        super.onCreate()
        _context = applicationContext
    }
}