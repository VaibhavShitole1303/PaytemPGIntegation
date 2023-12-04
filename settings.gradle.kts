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
        maven (
                url =("https://artifactory.paytm.in/libs-release-local")
                    )
    }
}

rootProject.name = "My Application"
include(":app")
 