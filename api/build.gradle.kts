plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "app.bambushain.api"
    compileSdk = 37

    defaultConfig {
        minSdk = 30

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.io.insert.koin.koin.bom))
    implementation(libs.io.insert.koin.koin.core)
    implementation(libs.io.insert.koin.koin.android)

    implementation(libs.org.jetbrains.kotlinx.kotlinx.datetime)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)

    implementation(platform(libs.retrofit2.bom))
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.kotlinx)
    implementation(libs.retrofit2.converter.scalars)
}