import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    kotlin("plugin.serialization") version "1.9.0"
    id("app.cash.sqldelight") version "2.0.2"

}

kotlin {
    androidTarget ()
//    {
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation ("androidx.compose.material3:material3:1.3.2")
            implementation ("androidx.compose.material3:material3-window-size-class:1.3.2")
            implementation ("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha14")

            implementation("app.cash.sqldelight:android-driver:2.0.2")

            implementation ("androidx.activity:activity-ktx:1.8.0")
            implementation("androidx.activity:activity-compose:1.8.0")
//            implementation ("androidx.activity:activity-ktx:1.8.0")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)



            implementation("org.jetbrains.compose.material3:material3:1.5.10")

            /* Corrutinas */
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

            /* Retrofit */
            implementation("com.squareup.retrofit2:retrofit:2.9.0")

            /* Gson */
            implementation("com.google.code.gson:gson:2.11.0")
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")

            /* Logging interceptor */
            implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

            /* Coil */
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

            implementation("com.squareup.retrofit2:retrofit:2.9.0")

            implementation("com.google.code.gson:gson:2.11.0")
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")

            implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

//            implementation ("com.github.bumptech.glide:glide:4.16.0")

//            implementation("androidx.room:room-ktx:2.6.1")
//            annotationProcessor("androidx.room:room-compiler:2.6.1")
//            implementation("androidx.room:room-runtime:2.6.1")
//
//            kapt("androidx.room:room-compiler:2.6.1")
//
//            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

            implementation("io.ktor:ktor-client-core:2.3.4")
            implementation("io.ktor:ktor-client-cio:2.3.4")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

            implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
            implementation("app.cash.sqldelight:sqlite-driver:2.0.2")

            implementation("io.ktor:ktor-client-logging:2.3.4")


        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("org.jetbrains.compose.ui:ui-util")
        }
    }
}

sqldelight {
    databases {
        create("ReviuBD") {
            packageName.set("com.exemple.db")

        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.ui.android)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.ui.text.google.fonts)
    debugImplementation(compose.uiTooling)


    implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.activity:activity-compose:1.8.0")

}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.AppImage, TargetFormat.Deb)
            packageName = "reviu"  // "org.example.project"
            packageVersion = "1.0.0"


        }
    }
}
