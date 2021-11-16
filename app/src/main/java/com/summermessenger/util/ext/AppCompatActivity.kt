package com.summermessenger.util.ext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.replaceFragment(containerViewId:Int, fragment: Fragment, addToBackStack:Boolean){
    val fragmentTransaction = supportFragmentManager.beginTransaction()

    if (addToBackStack)
        fragmentTransaction.addToBackStack(null)

    fragmentTransaction.replace(containerViewId, fragment)
        .commit()
}

fun AppCompatActivity.replaceFragment(containerView: FragmentContainerView, fragment: Fragment, addToBackStack: Boolean=true) {
    replaceFragment(containerView.id, fragment, addToBackStack)
}