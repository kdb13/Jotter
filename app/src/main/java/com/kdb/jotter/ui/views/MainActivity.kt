package com.kdb.jotter.ui.views

import android.os.Bundle
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
import com.kdb.jotter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Splash Screen
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        setupNavigation()
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

    override fun onActionModeStarted(mode: ActionMode?) {
        super.onActionModeStarted(mode)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)

        val statusBarColor = MaterialColors.getColor(binding.root, android.R.attr.statusBarColor)
        window.statusBarColor = statusBarColor
    }
}