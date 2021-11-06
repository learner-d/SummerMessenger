package com.summermessenger.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.summermessenger.databinding.ActivityMainBinding
import com.summermessenger.ui.chat.ChatActivity
import com.summermessenger.ui.login.ELoginState
import com.summermessenger.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private lateinit var mDrawer: MainDrawer

    private val mLoginActivityResultHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        if (it.resultCode != Activity.RESULT_OK) {
//            goToLogin()
//        }
    }

    private val mLoginStateObserver = Observer<ELoginState> {
        when(it) {
            ELoginState.LoggedIn -> {
//                goToChat()
            }
            ELoginState.LoggedOut -> {
                goToLogin()
            }
            else -> {}
        }
    }

    private fun goToLogin(){
        mLoginActivityResultHandler.launch(Intent(this, LoginActivity::class.java))
    }

    private fun goToChat(){
        val intent = Intent(this, ChatActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        startActivity(intent)
    }

    private fun logout(){
        mViewModel.logout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.chatButton.setOnClickListener {
            intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        mViewModel = ViewModelProvider(this, MainViewModelFactory())
            .get(MainViewModel::class.java)
        mDrawer = MainDrawer(this, mViewModel)
        setSupportActionBar(mBinding.mainToolbar)
        mDrawer.initialize(mBinding.mainToolbar)

        mViewModel.loginState.observe(this, mLoginStateObserver)
        mViewModel.loginDefault()
    }

    override fun onStart(){
        super.onStart()
        //initFields()
    }
}