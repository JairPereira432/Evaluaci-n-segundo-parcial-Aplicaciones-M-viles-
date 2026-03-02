// Archivo build.gradle.kts (Raíz del proyecto)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}