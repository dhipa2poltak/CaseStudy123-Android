// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.android.application") version "8.1.1" apply false
  id("org.jetbrains.kotlin.android") version "1.8.0" apply false
  id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
  id("com.android.library") version "8.1.1" apply false
  id("com.google.dagger.hilt.android") version "2.44" apply false
  id("jacoco")
}

jacoco {
  toolVersion = "0.8.11"
}

apply(from = "jacoco/project.gradle.kts")

subprojects {
  afterEvaluate {
    val projectName = this.name

    if (projectName != "features") {
      println("Applying JaCoCo for module-> $projectName")
      this.apply(from = "${this.rootDir}/jacoco/module.gradle.kts")
    }
  }
}

tasks.create(name = "codeCoverModules") {
  dependsOn(":domain:jacocoTestReport")
  dependsOn(":data:jacocoTestReport")
  dependsOn(":app:testProdDebugUnitTestCoverage")

  subprojects.forEach { proj ->
    val projectName = proj.name

    if (projectName != "domain" && projectName != "data" && projectName != "app") {
      if (projectName != "features") {
        if (projectName == "framework" || projectName == "navigation") {
          dependsOn(":${projectName}:testDebugUnitTestCoverage")
        } else {
          dependsOn(":features:${projectName}:testDebugUnitTestCoverage")
        }
      }
    }
  }
}

tasks.create<GradleBuild>(name = "allDebugCodeCoverage") {
  tasks = listOf("allDebugCoverage")
  mustRunAfter("codeCoverModules")
}

