plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.10"

    id("com.google.gms.google-services")
}


android {
    namespace = "com.pg.nityagirlshostel"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pg.nityagirlshostel"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ads.mobile.sdk)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.firebase.config.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("io.github.jan-tennert.supabase:supabase-kt:1.3.1")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.3.1")
    implementation("io.github.jan-tennert.supabase:storage-kt:1.3.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.ktor:ktor-client-cio:2.3.4")

    implementation("androidx.compose.ui:ui:1.6.0") // or the latest version
    implementation("androidx.compose.material3:material3:1.2.0") // if using Material3

    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.navigation:navigation-compose:2.7.5") // Or latest

    // Supabase client (via Ktor or custom)
    //implementation("io.ktor:ktor-client-core:2.3.2")
    implementation("io.ktor:ktor-client-okhttp:2.3.2")
    implementation("io.ktor:ktor-client-json:2.3.2")
    implementation("io.ktor:ktor-client-serialization:2.3.2")

// PDF generation
    implementation("androidx.core:core-ktx:1.10.1")

    // firebasse
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.android.gms:play-services-ads:22.6.0")





}