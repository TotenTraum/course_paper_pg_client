import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
//    alias(libs.plugins.cocoapods)
//    alias(libs.plugins.android.application)
    alias(libs.plugins.libres)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
//    android {
//        compilations.all {
//            kotlinOptions {
//                jvmTarget = "1.8"
//            }
//        }
//    }

    jvm("desktop")

//    js {
//        browser()
//        binaries.executable()
//    }

//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()

//    cocoapods {
//        version = "1.0.0"
//        summary = "Compose application framework"
//        homepage = "empty"
//        ios.deploymentTarget = "11.0"
//        podfile = project.file("../iosApp/Podfile")
//        framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation("org.slf4j:slf4j-simple:2.0.7")
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(libs.libres)
                implementation(libs.voyager.navigator)
                implementation(libs.composeImageLoader)
                implementation(libs.napier)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.core)
                implementation(libs.composeIcons.featherIcons)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.multiplatformSettings)
                implementation(libs.koin.core)
                implementation(libs.composeIcons.fontAwesome)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.auth)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

//        val androidMain by getting {
//            dependencies {
//                implementation(libs.androidx.appcompat)
//                implementation(libs.androidx.activityCompose)
//                implementation(libs.compose.uitooling)
//                implementation(libs.kotlinx.coroutines.android)
//                implementation(libs.ktor.client.okhttp)
//            }
//        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)
                implementation("io.ktor:ktor-client-cio:2.3.0")

            }
        }

//        val jsMain by getting {
//            dependencies {
//                implementation(compose.web.core)
//            }
//        }

//        val iosX64Main by getting
//        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
//        val iosMain by creating {
//            dependsOn(commonMain)
//            iosX64Main.dependsOn(this)
//            iosArm64Main.dependsOn(this)
//            iosSimulatorArm64Main.dependsOn(this)
//            dependencies {
//                implementation(libs.ktor.client.darwin)
//            }
//        }

//        val iosX64Test by getting
//        val iosArm64Test by getting
//        val iosSimulatorArm64Test by getting
//        val iosTest by creating {
//            dependsOn(commonTest)
//            iosX64Test.dependsOn(this)
//            iosArm64Test.dependsOn(this)
//            iosSimulatorArm64Test.dependsOn(this)
//        }
    }
}

//android {
//    namespace = "com.traum.client"
//    compileSdk = 33
//
//    defaultConfig {
//        minSdk = 21
//        targetSdk = 33
//
//        applicationId = "com.traum.client.androidApp"
//        versionCode = 1
//        versionName = "1.0.0"
//    }
//    sourceSets["main"].apply {
//        manifest.srcFile("src/androidMain/AndroidManifest.xml")
//        res.srcDirs("src/androidMain/resources")
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    packagingOptions {
//        resources.excludes.add("META-INF/**")
//    }
//}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.traum.client.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}

//compose.experimental {
//    web.application {}
//}

libres {
    // https://github.com/Skeptick/libres#setup
}
//tasks.getByPath("desktopProcessResources").dependsOn("libresGenerateResources")
//tasks.getByPath("desktopSourcesJar").dependsOn("libresGenerateResources")
//tasks.getByPath("jsProcessResources").dependsOn("libresGenerateResources")
dependencies {
    //    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
//    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
}

buildConfig {
  // BuildConfig configuration here.
  // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}
