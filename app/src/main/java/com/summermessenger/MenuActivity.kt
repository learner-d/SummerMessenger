package com.summermessenger

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
//import com.summermessenger.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    //private lateinit var mBinding: ActivityMenuBinding
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mBinding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_menu)
    }
}