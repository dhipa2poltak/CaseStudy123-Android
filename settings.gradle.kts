pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}

rootProject.name = "CaseStudy123"
include(":app")
include(":domain")
include(":data")
include(":framework")
include(":features:feature_splash")
include(":features:feature_error_message")
include(":features:feature_home")
include(":features:feature_qris")
include(":features:feature_portofolio")
include(":features:feature_qr_code_scanner")
