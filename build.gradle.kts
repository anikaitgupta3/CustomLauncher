// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.3.6" apply false // Check for the latest version on [GitHub](https://github.com/google/ksp) or [Android Developers documentation](https://developer.android.com/build/migrate-to-ksp)
    id("com.google.dagger.hilt.android") version "2.59.2" apply false
}