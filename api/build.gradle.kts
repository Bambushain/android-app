plugins {
    id("com.android.library")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "app.bambushain.api"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":models"))

    implementation("androidx.preference:preference:1.2.1")
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation("com.launchdarkly:okhttp-eventsource:4.1.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:3.0.0")
    implementation("com.squareup.retrofit2:converter-scalars:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("io.coil-kt:coil:2.7.0")
    implementation("io.coil-kt:coil-svg:2.7.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    //noinspection AnnotationProcessorOnCompilePath
    implementation("org.projectlombok:lombok:1.18.38")

    annotationProcessor("com.google.dagger:hilt-compiler:2.56.2")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}
