package com.chestlauncher.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chestlauncher.R
import com.chestlauncher.data.models.AppTheme
import com.chestlauncher.data.repository.LauncherRepository
import com.chestlauncher.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: LauncherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        repository = LauncherRepository(this)
        applyTheme(repository.getTheme())
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_accounts,
                R.id.navigation_mods,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun applyTheme(theme: AppTheme) {
        val styleRes = when (theme) {
            AppTheme.DARK      -> R.style.Theme_ChestLauncher_Dark
            AppTheme.MINECRAFT -> R.style.Theme_ChestLauncher_Minecraft
            AppTheme.MODERN    -> R.style.Theme_ChestLauncher_Modern
            AppTheme.COLORFUL  -> R.style.Theme_ChestLauncher_Colorful
            AppTheme.AMOLED    -> R.style.Theme_ChestLauncher_Amoled
            AppTheme.FOREST    -> R.style.Theme_ChestLauncher_Forest
            AppTheme.OCEAN     -> R.style.Theme_ChestLauncher_Ocean
        }
        setTheme(styleRes)
    }

    fun restartWithTheme() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
