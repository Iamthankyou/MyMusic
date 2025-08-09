// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Enforce Java/Kotlin toolchain 17 to avoid kapt issues on newer JDKs
allprojects {
    extensions.findByName("kotlin")?.let {
        try {
            @Suppress("UNCHECKED_CAST")
            val ext = it as org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
            ext.jvmToolchain(17)
        } catch (_: Throwable) {}
    }
}