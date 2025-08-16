import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.google.services)
}

val bambooMajorVersion: String by project
val bambooMinorVersion: String by project

fun computeVersionCode(): Int {
    val bambooReleaseVersion = System.getenv("CI_PIPELINE_IID") ?: "1"
    val versionCode =
        buildString {
            append("10")
            append(
                ((bambooMajorVersion.toInt() * 100000) + (bambooMinorVersion.toInt() * 10000) + bambooReleaseVersion.toInt()).toString(
                    10
                )
            )
        }.toInt()

    print("Versionname is ${bambooMajorVersion}.${bambooMinorVersion}.${bambooReleaseVersion}")
    print("Versioncode is $versionCode")

    return versionCode
}

android {
    namespace = "app.bambushain"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.bambushain"
        minSdk = 28
        targetSdk = 36
        versionCode = computeVersionCode()
        versionName = "2.0"
    }

    signingConfigs {
        create("release") {
            storeFile =
                file(System.getenv("ANDROID_KEY_STOREFILE") ?: "/opt/secure/signing-key-jewels.jks")
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: "key0"
            keyPassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isCrunchPngs = true
            isShrinkResources = true
            isProfileable = false
            isJniDebuggable = false
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_23
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation)

    implementation(libs.androidx.preferences.ktx)

    implementation(platform(libs.io.insert.koin.koin.bom))
    implementation(libs.io.insert.koin.koin.core)
    implementation(libs.io.insert.koin.koin.compose)
    implementation(libs.io.insert.koin.koin.android)
    implementation(libs.io.insert.koin.koin.androidx.compose)
    implementation(libs.io.insert.koin.koin.androidx.workmanager)

    implementation(libs.io.ktor.ktor.client.core)
    implementation(libs.io.ktor.ktor.client.okhttp)
    implementation(libs.io.ktor.ktor.client.content.negotiation)
    implementation(libs.io.ktor.ktor.serialization.kotlinx.json)

    implementation(platform(libs.io.coil.kt.coil3.coil.bom))
    implementation(libs.io.coil.kt.coil3.coil.core)
    implementation(libs.io.coil.kt.coil3.coil.compose)
    implementation(libs.io.coil.kt.coil3.coil.network.okhttp)
    implementation(libs.io.coil.kt.coil3.coil.network.ktor3)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.fcm)
}