buildscript {
    dependencies {
//        classpath("com.android.tools.build:gradle:7.4.1")
    }
}
plugins {
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.compose).apply(false)
//    alias(libs.plugins.cocoapods).apply(false)
//    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.libres).apply(false)
    alias(libs.plugins.buildConfig).apply(false)
    alias(libs.plugins.kotlinx.serialization).apply(false)
}
