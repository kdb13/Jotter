package com.kdb.jotter.ui.views

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.MaterialColors
import com.kdb.jotter.R
import com.kdb.jotter.TAG
import com.kdb.jotter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var showSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Splash Screen
        installSplashScreen().apply {
             setKeepOnScreenCondition { showSplashScreen }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        setupNavigation()

        setFragmentListener()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: MainActivity")
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: MainActivity")
    }
    
    private fun setupNavigation() {
        // Initialize the navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the Toolbar with Navigation Component
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setFragmentListener() {
        supportFragmentManager
            .setFragmentResultListener(
                FRAGMENT_RESULT_REQUEST_KEY, this
            ) { _, bundle ->
                val result = bundle.getBoolean(SPLASH_SCREEN_RESULT_KEY)
                if (result) showSplashScreen = false
            }
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        super.onActionModeStarted(mode)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)

        val statusBarColor = MaterialColors.getColor(binding.root, android.R.attr.statusBarColor)
        window.statusBarColor = statusBarColor
    }
    
    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "com.kdb.jotter.FRAGMENT_RESULT_REQUEST"
        const val SPLASH_SCREEN_RESULT_KEY = "com.kdb.jotter.SPLASH_SCREEN_RESULT"
    }
}