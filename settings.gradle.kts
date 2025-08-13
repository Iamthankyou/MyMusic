pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        // Thêm mirror repositories để giải quyết vấn đề kết nối
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.devtools.ksp") {
                useModule("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.24-1.0.19")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Thêm mirror repositories cho dependencies
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
    }
}

rootProject.name = "MyMusic"
include(":app")

// Enable automatic Java toolchain provisioning (downloads required JDKs)
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
 