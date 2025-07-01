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

    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.core:core:1.16.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.fragment:fragment:1.8.8")
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.1")
    implementation("androidx.navigation:navigation-fragment:2.9.0")
    implementation("androidx.navigation:navigation-ui:2.9.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-rxjava3:2.7.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.github.requery:sqlite-android:3.49.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation("com.launchdarkly:okhttp-eventsource:4.1.1")
    implementation("com.squareup.retrofit2:adapter-rxjava3:3.0.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    //noinspection AnnotationProcessorOnCompilePath
    implementation("org.projectlombok:lombok:1.18.38")

    annotationProcessor("androidx.databinding:databinding-compiler:8.11.0")
    annotationProcessor("androidx.room:room-compiler:2.7.2")
    annotationProcessor("com.google.dagger:hilt-compiler:2.56.2")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}
