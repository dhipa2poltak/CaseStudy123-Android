import org.gradle.api.JavaVersion

object ConfigData {
  const val compileSdkVersion = 34
  const val minSdkVersion = 21
  const val targetSdkVersion = 34
  const val versionCode = 3
  const val versionName = "1.3"
  const val versionCodeDev = 3
  const val versionNameDev = "1.3"
  val sourceCompatibilityVersion = JavaVersion.VERSION_17
  val targetCompatibilityVersion = JavaVersion.VERSION_17
  const val jvmTargetVersion = "17"
}
