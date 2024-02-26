plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.dagger.hilt.android")
  id("kotlin-kapt")
}

android {
  namespace = "com.dpfht.android.casestudy123"
  compileSdk = ConfigData.compileSdkVersion

  defaultConfig {
    minSdk = ConfigData.minSdkVersion
    targetSdk = ConfigData.targetSdkVersion

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true

      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      manifestPlaceholders["appNameSuffix"] = ""
      resValue("string", "running_mode", "")
    }
    debug {
      isMinifyEnabled = false
      isShrinkResources = false

      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"

      manifestPlaceholders["appNameSuffix"] = "-(debug)"
      resValue("string", "running_mode", "-(debug)")
    }
  }

  flavorDimensions.add("default")

  productFlavors {
    create("prod") {
      applicationId = "com.dpfht.android.casestudy123"
      versionCode = ConfigData.versionCode
      versionName = ConfigData.versionName

      manifestPlaceholders["appName"] = "Case Study 123"
      resValue("string", "app_name", "Case Study 123")
    }
    create("dev") {
      applicationId = "com.dpfht.android.casestudy123.dev"
      versionCode = ConfigData.versionCodeDev
      versionName = ConfigData.versionNameDev

      manifestPlaceholders["appName"] = "Case Study 123 (DEV)"
      resValue("string", "app_name", "Case Study 123 (DEV)")
    }
  }

  buildFeatures {
    buildConfig = true
    viewBinding = true
    dataBinding = true
  }
  compileOptions {
    sourceCompatibility = ConfigData.sourceCompatibilityVersion
    targetCompatibility = ConfigData.targetCompatibilityVersion
  }
  kotlinOptions {
    jvmTarget = ConfigData.jvmTargetVersion
  }
}

dependencies {

  implementation(project(":framework"))
  implementation(project(":navigation"))

  implementation(Deps.coreKtx)
  implementation(Deps.appCompat)
  implementation(Deps.material)
  implementation(Deps.constraintLayout)
  testImplementation(Deps.jUnit)
  androidTestImplementation(Deps.jUnitExt)
  androidTestImplementation(Deps.espresso)

  implementation(Deps.navigationFragment)
  implementation(Deps.navigationUi)

  implementation(Deps.hilt)
  kapt(Deps.hiltCompiler)
}
