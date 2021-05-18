package com.summermessenger

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import android.renderscript.ScriptGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.summermessenger.databinding.ActivityMenuBinding
import com.summermessenger.ui.chat.ChatActivity


class MenuActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMenuBinding
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar
    private lateinit var _chat_button: Button





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        _chat_button = findViewById(R.id.chat_button)



        _chat_button.setOnClickListener { v ->
            intent = Intent(this, ChatActivity ::class.java )
            startActivity(intent)

        }







    }



    override fun onStart(){
        super.onStart()
        initFileds()
        initFunction()
    }

    private fun initFunction() {
        setSupportActionBar(mToolbar)
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(mToolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName("Создать группу")
                    .withSelectable(false)
            ) .build()
    }


    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem().withName("Volkov Albert")
                    .withEmail("+380992527808")
            ).build()
    }

    private fun initFileds(){
        mToolbar = mBinding.mainToolbar
    }
}