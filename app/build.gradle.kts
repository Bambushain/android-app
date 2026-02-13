plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
}

val bambooMajorVersion: String by project
val bambooMinorVersion: String by project

fun computeVersionCode(): Int {
    val bambooReleaseVersion = System.getenv("CI_PIPELINE_IID") ?: "1"
    val versionCode =
        ((bambooMajorVersion.toInt() * 100000) + (bambooMinorVersion.toInt() * 10000) + bambooReleaseVersion.toInt())

    return versionCode
}

android {
    namespace = "app.bambushain"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.bambushain"
        minSdk = 30
        targetSdk = 36
        versionCode = computeVersionCode()
        versionName = "$bambooMajorVersion.$bambooMinorVersion"
    }

    signingConfigs {
        create("release") {
            storeFile =
                file(System.getenv("ANDROID_KEY_STOREFILE") ?: "/opt/secure/signing-key-bambushain.jks")
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
            isMinifyEnabled = false
            isCrunchPngs = true
            isShrinkResources = false
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    lint {
        disable += listOf("CheckResult", "ResultOfMethodCallIgnored")
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":models"))

    implementation("androidx.activity:activity:1.12.4")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.10.0")
    implementation("androidx.fragment:fragment:1.8.9")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.navigation:navigation-fragment:2.9.7")
    implementation("androidx.navigation:navigation-ui:2.9.7")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-rxjava3:2.8.4")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")
    implementation("com.github.requery:sqlite-android:3.49.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.google.dagger:hilt-android:2.59.1")
    implementation("com.launchdarkly:okhttp-eventsource:4.2.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:3.0.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.12")
    //noinspection AnnotationProcessorOnCompilePath
    implementation("org.projectlombok:lombok:1.18.42")

    annotationProcessor("androidx.databinding:databinding-compiler:9.0.0")
    annotationProcessor("androidx.room:room-compiler:2.8.4")
    annotationProcessor("com.google.dagger:hilt-compiler:2.59.1")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}
