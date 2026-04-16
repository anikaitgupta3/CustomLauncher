import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // ... other plugins like kotlin-android
    id("com.google.devtools.ksp") // Apply the KSP plugin
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.3.20"

}
val localProps = Properties()
val localPropertiesFile = File(rootProject.rootDir,"local.properties")
if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
    localPropertiesFile.inputStream().use {
        localProps.load(it)
    }
}
android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\Admin\\mykeystore.jks")
            storePassword = "Anikait3#"
            keyAlias = "key0"
            keyPassword = "Anikait3#"
        }
    }
    namespace = "com.anikaitgupta.weatherlauncher"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.anikaitgupta.weatherlauncher"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            //isMinifyEnabled = false
            isMinifyEnabled = true // This enables R8/ProGuard
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_KEY", localProps.getProperty("API_KEY"))
            signingConfig = signingConfigs.getByName("debug")// IF any
        }
        debug {
            buildConfigField("String", "API_KEY", localProps.getProperty("API_KEY"))// IF any
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    // Modern way to set jvmTarget
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.accompanist.drawablepainter)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.androidx.datastore.preferences)
    //val roomVersion = "2.6.1" // Check for the latest stable version on the [Android Developers site](https://developer.android.com/jetpack/androidx/releases/room)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // For Kotlin extensions and coroutines support
    ksp(libs.androidx.room.compiler) // Use KSP for annotation processing
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.gson)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("io.coil-kt.coil3:coil-compose:3.4.0")
    // Source: https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // ADD THIS: Required for Coil 3 to handle URLs/Network images
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.4.0")





//0.28.0
}