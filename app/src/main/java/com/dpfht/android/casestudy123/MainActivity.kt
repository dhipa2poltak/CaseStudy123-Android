package com.dpfht.android.casestudy123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.dpfht.android.casestudy123.databinding.ActivityMainBinding
import com.dpfht.android.casestudy123.framework.R as frameworkR
import com.dpfht.android.casestudy123.navigation.R as navigationR
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

    appBarConfiguration = AppBarConfiguration(setOf(navigationR.id.homeFragment))

    val navHostFragment = supportFragmentManager.findFragmentById(frameworkR.id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController

    navController.addOnDestinationChangedListener { _, destination, _ ->
      title = when (destination.id) {
        navigationR.id.homeFragment -> "${getString(R.string.app_name)}${getString(R.string.running_mode)}"
        navigationR.id.qrCodeScannerFragment -> resources.getString(R.string.app_text_scan_a_qr_code)
        navigationR.id.qrisHistoryFragment -> resources.getString(R.string.app_text_history)
        navigationR.id.portofolioFragment -> resources.getString(R.string.app_text_chart)
        else -> "${getString(R.string.app_name)}${getString(R.string.running_mode)}"
      }
    }

    setupActionBarWithNavController(navController, appBarConfiguration)
  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}
