package com.dpfht.android.casestudy123

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheApplication: Application() {

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    lateinit var instance: TheApplication
  }
}
