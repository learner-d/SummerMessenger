package com.summermessenger.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.summermessenger.R
import com.summermessenger.data.MainRepository
import com.summermessenger.ui.chat.ChatActivity
import com.summermessenger.ui.login.LoginActivity

const val LOGIN_REQUEST_CODE = 501
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (MainRepository.loginRepository.isLoggedIn)
            goToChats()
        else
            goToLogin()

        setContentView(R.layout.activity_main)
    }

    private fun goToLogin(){
        startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN_REQUEST_CODE)
    }

    private fun goToChats(){
        val intent = Intent(this, ChatActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                goToChats()
                finish()
            }
            else
                goToLogin()
        }
    }
}