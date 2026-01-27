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
        maven("https://jitpack.io") // Corrected syntax for Maven URL
        jcenter() // Warning: this repository is going to shut down soon
    }
}

rootProject.name = "Hisaab Analyser"
include(":app")
