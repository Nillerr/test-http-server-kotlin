rootProject.name = "test-http-server"

pluginManagement {
    repositories {
        mavenCentral()
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        kotlin("jvm") version kotlinVersion apply false
        id("org.jetbrains.dokka") version kotlinVersion apply false
    }
}

include(":test-http-server")
include(":test-http-server-jackson")
