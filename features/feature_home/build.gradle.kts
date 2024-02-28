plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.google.dagger.hilt.android")
  id("kotlin-kapt")
}

android {
  namespace = "com.dpfht.android.casestudy123.feature_home"
  compileSdk = ConfigData.compileSdkVersion

  defaultConfig {
    minSdk = ConfigData.minSdkVersion

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  buildFeatures {
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

  implementation(project(":domain"))
  implementation(project(":framework"))

  implementation(Deps.coreKtx)
  implementation(Deps.appCompat)
  implementation(Deps.material)
  testImplementation(Deps.jUnit)
  testImplementation(Deps.coroutinesTest)
  testImplementation(Deps.mockitoKotlin)
  testImplementation(Deps.mockitoInline)
  testImplementation(Deps.coreTesting)
  androidTestImplementation(Deps.jUnitExt)
  androidTestImplementation(Deps.espresso)

  implementation(Deps.hilt)
  kapt(Deps.hiltCompiler)

  implementation(Deps.fragmentKtx)
  implementation(Deps.lifecycleViewModel)

  implementation(Deps.swipeRefreshLayout)
}
