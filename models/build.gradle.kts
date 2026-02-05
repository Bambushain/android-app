plugins {
    id("com.android.library")
}

android {
    namespace = "app.bambushain.models"
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
    implementation("com.google.code.gson:gson:2.13.2")
    //noinspection AnnotationProcessorOnCompilePath
    implementation("org.projectlombok:lombok:1.18.42")

    annotationProcessor("org.projectlombok:lombok:1.18.42")
}