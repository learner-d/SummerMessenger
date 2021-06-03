package com.summermessenger.util.ext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.replaceFragment(containerViewId:Int, fragment: Fragment, toBackStack:Boolean=true){
    val t = supportFragmentManager.beginTransaction()

    if (toBackStack)
       t.addToBackStack(null)

    t.replace(containerViewId, fragment)
        .commit()
}