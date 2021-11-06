package com.summermessenger.ui.main

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.mikepenz.iconics.Iconics.applicationContext
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.summermessenger.R
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.ui.login.ELoginState

class MainDrawer(private val mMainActivity: AppCompatActivity,
                 private val mMainViewModel: MainViewModel) {
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawer: Drawer

    private val mLoginStateObserver = Observer<ELoginState> {
        when(it) {
            ELoginState.LoggedIn -> {
                onUserLoggedIn()
            }
            ELoginState.LoggedOut -> {
                onUserLoggedOut()
            }
            else -> { }
        }
    }

    fun initialize(toolbar: Toolbar) {
        createHeader()
        createDrawer(toolbar)
        mMainViewModel.loginState.observe(mMainActivity, mLoginStateObserver)
    }
    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(mMainActivity)
            .withHeaderBackground(R.drawable.header).build()
    }
    private fun createDrawer(toolbar: Toolbar) {
        mDrawer = DrawerBuilder()
            .withActivity(mMainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName("Create Group")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_group),

                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName("Contacts")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_person_24),

                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName("Settings")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_settings_24),

                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName("Favourite")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_turned_in_24),

                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName("About")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_eco_24),

                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName("Log out")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_west_24)

            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(drawerItem.identifier){
                        105L -> {
                            mMainViewModel.logout()
                        }
                        else -> {
                            Toast.makeText(applicationContext,position.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    return false
                }

            })
            .build()
    }
    private fun onUserLoggedIn() {
        val currentUser = MainRepository.usersRepository.loggedInUser
            ?: return
        mHeader.activeProfile = ProfileDrawerItem()
            .withName(currentUser.displayName)
            .withEmail(currentUser.phoneNumber)
    }
    private fun onUserLoggedOut() {
        mHeader.activeProfile = null
    }
}