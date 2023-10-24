package com.dpfht.android.casestudy123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.dpfht.android.casestudy123.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))

    val navHostFragment = supportFragmentManager.findFragmentById(R.id.demo_nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController

    navController.addOnDestinationChangedListener { _, destination, _ ->
      title = when (destination.id) {
        R.id.homeFragment -> "${getString(R.string.app_name)}${getString(R.string.running_mode)}"
        R.id.qrisHistoryFragment -> resources.getString(R.string.app_text_history)
        R.id.portofolioFragment -> resources.getString(R.string.app_text_chart)
        else -> "${getString(R.string.app_name)}${getString(R.string.running_mode)}"
      }
    }

    setupActionBarWithNavController(navController, appBarConfiguration)
  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}
