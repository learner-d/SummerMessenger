package com.summermessenger.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.summermessenger.R
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.databinding.ActivityMainBinding
import com.summermessenger.ui.chat.ChatActivity
import com.summermessenger.ui.login.LoginActivity

const val LOGIN_REQUEST_CODE = 501
class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.chatButton.setOnClickListener { v ->
            intent = Intent(this, ChatActivity ::class.java )
            startActivity(intent)
        }
    }

    override fun onStart(){
        super.onStart()
        initFields()
        initFunction()
    }

    private fun initFields(){
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initFunction() {
        if (mAuth.currentUser == null && !MainRepository.usersRepository.isLoggedIn)
            goToLogin()
        setSupportActionBar(mBinding.mainToolbar)
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(mBinding.mainToolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                        PrimaryDrawerItem().withIdentifier(100)
                                .withIconTintingEnabled(true)
                                .withName("Создать группу")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_group),

                        PrimaryDrawerItem().withIdentifier(101)
                                .withIconTintingEnabled(true)
                                .withName("Контакты")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_baseline_person_24),

                        PrimaryDrawerItem().withIdentifier(102)
                                .withIconTintingEnabled(true)
                                .withName("Настройки")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_baseline_settings_24),

                        PrimaryDrawerItem().withIdentifier(103)
                                .withIconTintingEnabled(true)
                                .withName("Избранное")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_baseline_turned_in_24),

                        PrimaryDrawerItem().withIdentifier(104)
                                .withIconTintingEnabled(true)
                                .withName("О нас")
                                .withSelectable(false)
                                .withIcon(R.drawable.ic_baseline_eco_24)

                ) .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener{
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        Toast.makeText(applicationContext,position.toString(), Toast.LENGTH_SHORT).show()
                        return false
                    }

                })
                .build()
    }

    private fun createHeader() {
        var loggedInUser = MainRepository.usersRepository.loggedInUser
        mHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        ProfileDrawerItem().withName(loggedInUser?.displayName ?: "(null)")
                                .withEmail(loggedInUser?.phoneNumber ?: "")
                ).build()
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
            if (resultCode != Activity.RESULT_OK) {
                goToLogin()
            }
        }
    }
}