package com.dpfht.android.casestudy123.navigation.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dpfht.android.casestudy123.framework.navigation.NavigationService
import com.dpfht.android.casestudy123.framework.R as frameworkR
import com.dpfht.android.casestudy123.navigation.NavigationServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class NavigationModule {

  @Provides
  fun provideNavController(@ActivityContext context: Context): NavController {
    val navHostFragment =  (context as AppCompatActivity).supportFragmentManager.findFragmentById(frameworkR.id.nav_host_fragment) as NavHostFragment

    return navHostFragment.navController
  }

  @Provides
  fun provideNavigationService(navController: NavController, @ActivityContext context: Context): NavigationService {
    return NavigationServiceImpl(navController, context as AppCompatActivity)
  }
}
