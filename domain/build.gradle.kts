plugins {
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
}

java {
  sourceCompatibility = ConfigData.sourceCompatibilityVersion
  targetCompatibility = ConfigData.targetCompatibilityVersion
}

dependencies {
  implementation(Deps.rxKotlin)

  testImplementation(Deps.jUnit)
  testImplementation(Deps.mockitoKotlin)
  testImplementation(Deps.mockitoInline)
  testImplementation(Deps.coroutinesTest)
}
