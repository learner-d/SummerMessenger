package com.summermessenger.ui.main

import android.content.ContentValues.TAG
import android.util.Log
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
import com.summermessenger.data.model.User
import com.summermessenger.ui.login.ELoginState
import com.summermessenger.ui.login.LoginResult

class MainDrawer(private val mMainActivity: AppCompatActivity,
                 private val mMainViewModel: MainViewModel) {
    companion object {
        const val DRAWER_ITEM_ADDUSER       = 100L
        const val DRAWER_ITEM_CREATEGROUP   = 101L
        const val DRAWER_ITEM_CONTACTS      = 102L
        const val DRAWER_ITEM_SETTINGS      = 103L
        const val DRAWER_ITEM_FAVORITE      = 104L
        const val DRAWER_ITEM_ABOUT         = 105L
        const val DRAWER_ITEM_LOGOUT        = 106L
    }

    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawer: Drawer

    private val mLoginStateObserver = Observer<LoginResult> {
        when(it.loginState) {
            ELoginState.LoggedIn -> {
                onUserLoggedIn(it.user)
            }
            ELoginState.LoggedOut -> {
                onUserLoggedOut(it.user)
            }
            else -> { }
        }
    }

    fun initialize(toolbar: Toolbar) {
        createHeader()
        createDrawer(toolbar)
        mMainViewModel.loginResult.observe(mMainActivity, mLoginStateObserver)
    }
    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(mMainActivity)
            .withAlternativeProfileHeaderSwitching(true)
            .withHeaderBackground(R.drawable.header)
            .build()
    }
    private fun createDrawer(toolbar: Toolbar) {
        mDrawer = DrawerBuilder()
            .withActivity(mMainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_ADDUSER)
                    .withIconTintingEnabled(true)
                    .withName("Add User")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_person_add_24),
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_CREATEGROUP)
                    .withIconTintingEnabled(true)
                    .withName("Create Group")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_group),
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_CONTACTS)
                    .withIconTintingEnabled(true)
                    .withName("Contacts")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_person_24),
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_SETTINGS)
                    .withIconTintingEnabled(true)
                    .withName("Settings")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_settings_24),
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_FAVORITE)
                    .withIconTintingEnabled(true)
                    .withName("Favourite")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_turned_in_24),
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_ABOUT)
                    .withIconTintingEnabled(true)
                    .withName("About")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_eco_24),
                PrimaryDrawerItem().withIdentifier(DRAWER_ITEM_LOGOUT)
                    .withIconTintingEnabled(true)
                    .withName("Log out")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_west_24)

            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(drawerItem.identifier){
                        DRAWER_ITEM_ADDUSER -> {
                            onAddUserClick()
                        }
                        DRAWER_ITEM_LOGOUT -> {
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
    private fun onAddUserClick() {
        mMainViewModel.requestNewLogin()
    }
    private fun onUserLoggedIn(user: User?) {
        user ?: return
        // Перевірити наявність користувача в списку
        val alreadyLoggedInUser = findUserProfileItem(user.userId)
        if (alreadyLoggedInUser != null) {
            val warnMessage = "user ${user.userId} is in accounts list already"
            Log.w(TAG, warnMessage)
//            Toast.makeText(mMainActivity, s, Toast.LENGTH_LONG).show()
            mHeader.activeProfile = alreadyLoggedInUser
            return
        }
        // Додати користувача
        val userProfileItem = ProfileDrawerItem()
            .withTag(user.userId)
            .withName(user.displayName)
            .withEmail(user.phoneNumber)
        mHeader.addProfiles(userProfileItem)
        mHeader.activeProfile = userProfileItem
    }
    private fun onUserLoggedOut(user: User?) {
        user ?: return
        val userProfileItem = findUserProfileItem(user.userId)
        if(userProfileItem == null) {
            Toast.makeText(mMainActivity, "user ${user.userId} is not in account list", Toast.LENGTH_LONG).show()
            return
        }

        mHeader.removeProfile(userProfileItem)

        // Вилучити користувача
        mHeader.activeProfile = if (mHeader.profiles?.isNotEmpty() == true)
            mHeader.profiles?.get(0)
        else
            null

    }
    private fun findUserProfileItem(userId: String?) : ProfileDrawerItem? {
        return mHeader.profiles?.find {
            (it as ProfileDrawerItem).tag?.equals(userId) ?: false
        } as ProfileDrawerItem?
    }
}