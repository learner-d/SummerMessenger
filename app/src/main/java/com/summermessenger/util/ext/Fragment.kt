package com.summermessenger.util.ext

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message:String, duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this.context, message, duration).show()
}

fun Fragment.replaceFragment(containerViewId:Int, fragment: Fragment){
    this.parentFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(containerViewId, fragment)
        .commit()
}