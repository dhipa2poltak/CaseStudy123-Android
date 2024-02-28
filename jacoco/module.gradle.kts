apply(plugin = "jacoco")

tasks.withType<Test> {
  configure<JacocoTaskExtension> {
    isIncludeNoLocationClasses = true
    excludes = listOf("jdk.internal.*")
  }
}

fun isAndroidModule(project: Project): Boolean {
  val isAndroidLibrary: Boolean = project.plugins.hasPlugin("com.android.library")
  val isAndroidApp: Boolean = project.plugins.hasPlugin("com.android.application")
  return isAndroidLibrary || isAndroidApp
}

afterEvaluate {
  if (isAndroidModule(this))
    setupAndroidReporting(this)
  else
    setupKotlinReporting(this)
}

fun setupKotlinReporting(project: Project) {
  tasks.getByName<JacocoReport>("jacocoTestReport") {
    dependsOn("test")

    reports {
      html.required.set(true)
      xml.required.set(true)
    }

    val fileFilter = mutableSetOf(
      // dagger
      "**/*_MembersInjector.class",
      "**/Dagger*Component.class",
      "**/Dagger*Component\$Builder.class",
      "**/Dagger*Subcomponent*.class",
      "**/*Subcomponent\$Builder.class",
      "**/*Module_*Factory.class",
      "**/di/module/*",
      "**/*_Factory*.*",
      "**/*Module*.*",
      "**/*Dagger*.*",
      "**/*Hilt*.*")

    afterEvaluate {
      val dirs = arrayListOf<String>()
      classDirectories.files.forEach {
        val tmp = (fileTree(it) { exclude(fileFilter) }).getDir().getAbsolutePath()
        dirs.add(tmp)
      }

      classDirectories.setFrom(files(dirs))
    }
  }
}

fun setupAndroidReporting(project: Project) {
  val buildTypes = arrayOf("debug", "release")
  val productFlavors = if (project.name == "app") {
    arrayListOf("dev", "prod")
  } else {
    arrayListOf<String>()
  }

  // When no product flavors defined, use empty
  if (productFlavors.isEmpty()) productFlavors.add("")

  productFlavors.forEach { productFlavorName ->
    buildTypes.forEach { buildTypeName ->
      val sourceName: String
      val sourcePath: String

      if (productFlavorName.isEmpty()) {
        sourceName = "${buildTypeName}"
        sourcePath = sourceName
      } else {
        sourceName = "${productFlavorName}${buildTypeName.capitalize()}"
        sourcePath = "${productFlavorName}/${buildTypeName}"
      }
      val testTaskName = "test${sourceName.capitalize()}UnitTest"
      System.out.println("Task -> $testTaskName")

      // Create coverage task of form "testFlavorTypeCoverage" depending on "testFlavorTypeUnitTest"
      tasks.create<JacocoReport>(name = "${testTaskName}Coverage") {
        dependsOn("$testTaskName")

        group = "Reporting"
        description = "Generate Jacoco coverage reports on the ${sourceName.capitalize()} build."

        reports {
          html.required.set(true)
          xml.required.set(true)
        }

        val fileFilter = mutableSetOf(
            // data binding
            "android/databinding/**/*.class",
            "**/android/databinding/*Binding.class",
            "**/android/databinding/*",
            "**/androidx/databinding/*",
            "**/databinding/*",
            "**/BR.*",
            "**/DataBinding*.*",
            // android("**/R.class"),
            "**/R\$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            // dagger
            "**/*_MembersInjector.class",
            "**/Dagger*Component.class",
            "**/Dagger*Component\$Builder.class",
            "**/Dagger*Subcomponent*.class",
            "**/*Subcomponent\$Builder.class",
            "**/*Module_*Factory.class",
            "**/di/module/*",
            "**/*_Factory*.*",
            "**/*Module*.*",
            "**/*Dagger*.*",
            "**/*Hilt*.*",
            "hilt*",
            "dagger*",
            // kotlin
            "**/*MapperImpl*.*",
            "**/*\$ViewInjector*.*",
            "**/*\$ViewBinder*.*",
            "**/BuildConfig.*",
            "**/*Component*.*",
            "**/*BR*.*",
            "**/Manifest*.*",
            "**/*\$Lambda$*.*",
            "**/*Companion*.*",
            "**/*Module*.*",
            "**/*Dagger*.*",
            "**/*Hilt*.*",
            "**/*MembersInjector*.*",
            "**/*_MembersInjector.class",
            "**/*_Factory*.*",
            "**/*_Provide*Factory*.*",
            "**/*Extensions*.*",
            // sealed and data classes
            "**/*\$Result.*",
            "**/*\$Result$*.*",
            // adapters generated by moshi
            "**/*JsonAdapter.*",
            "**/*Fragment.*",
            "**/*Fragment*.*",
            "**/*Activity.*",
            "**/*Activity*.*",
            "**/*Adapter.*",
            "**/*Adapter*.*",
            "**/TheApplication*.*",
            "**/framework/Config.*",
            "**/analyzer/*",
            "**/framework/commons/model/*",
            "**/feature_home/model/*"
        )

        val javaTree = (fileTree("${project.buildDir}/intermediates/javac/$sourceName/classes") { exclude(fileFilter) }).getDir().getAbsolutePath()
        val kotlinTree = (fileTree("${project.buildDir}/tmp/kotlin-classes/$sourceName") { exclude(fileFilter) }).getDir().getAbsolutePath()

        classDirectories.setFrom(files(listOf(javaTree, kotlinTree)))
        classDirectories.setFrom(files(classDirectories.files.map {
          fileTree(it).apply {
            exclude(fileFilter)
          }
        }))

        executionData.setFrom(files("${project.buildDir}/jacoco/${testTaskName}.exec"))
        val coverageSourceDirs = listOf(
            "src/main/java",
            "src/$productFlavorName/java",
            "src/$buildTypeName/java"
        )

        sourceDirectories.setFrom(files(coverageSourceDirs))
        additionalSourceDirs.setFrom(files(coverageSourceDirs))
      }
    }
  }
}
