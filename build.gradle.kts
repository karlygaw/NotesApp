// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    dependencies {
        // Directly set the Kotlin version here
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

