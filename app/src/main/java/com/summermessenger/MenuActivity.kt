package com.summermessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.summermessenger.databinding.ActivityMenuBinding
import com.summermessenger.ui.chat.ChatActivity


class MenuActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMenuBinding
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar
    private lateinit var mChatButton: Button





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mChatButton = findViewById(R.id.chat_button)



        mChatButton.setOnClickListener { v ->
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

                    ) .withOnDrawerItemClickListener(object :Drawer.OnDrawerItemClickListener{
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>):

                            Boolean {
                        Toast.makeText(applicationContext,position.toString(),Toast.LENGTH_SHORT).show()
                        return false
                    }

                })


                .build()
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