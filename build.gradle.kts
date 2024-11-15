

buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath(libs.google.services.v4315)

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
}
repositories {
    google()
    mavenCentral()
}
