pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                mavenCentral()
                maven { url = uri("https://jitpack.io") }
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Notes"
include(":app")
include(":core")
include(":data")
include(":core:ui")
include(":core:network")
include(":core:utility")
include(":feature")
include(":feature:home")
include(":feature:settings")
include(":feature:newrecipe")
include(":feature:addmeal")
include(":feature:foodmanager")
include(":feature:statistic")
include(":feature:programmanager")
include(":feature:programexecution")
include(":feature:splash")
include(":data:diet")
include(":data:datastore")
include(":data:program")
include(":data:training")
include(":data:worker")
