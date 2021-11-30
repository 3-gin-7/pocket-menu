package com.example.pocketmenu

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


// logging
private val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var nav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progBar)
        progressBar.isVisible = false

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFF00")))
        supportActionBar?.title = Html.fromHtml("<font color='#000'>PocketMenu </font>")

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nav = findViewById(R.id.bottomNavigationView);
        nav.isVisible = false;
        navigation = nav

        val bottomNavigationView: BottomNavigationView = this.findViewById(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }


    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when(item.itemId){
            R.id.homeFragment -> {
                Log.i(TAG, "home")
                true
            }
            R.id.cartFragment -> {
                Log.i(TAG, "cart")
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top_nav, menu);
        if(menu != null){
            menuItem = menu.findItem(R.id.logout)
            menuItem.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> {
                menuItem.isVisible = false

                this.findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        lateinit var navigation:BottomNavigationView
        lateinit var menuItem:MenuItem
        lateinit var progressBar: ConstraintLayout
    }
}