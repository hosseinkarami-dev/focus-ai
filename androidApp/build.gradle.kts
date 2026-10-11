import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.core)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "io.github.hosseinkarami_dev.focusai"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.hosseinkarami_dev.focusai"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["aiBackendUrl"] = providers.gradleProperty("focusAiBackendUrl").orNull ?: ""
    }
    val releaseStoreFile = providers.environmentVariable("FOCUSAI_ANDROID_KEYSTORE").orNull
    val releaseStorePassword = providers.environmentVariable("FOCUSAI_ANDROID_STORE_PASSWORD").orNull
    val releaseKeyAlias = providers.environmentVariable("FOCUSAI_ANDROID_KEY_ALIAS").orNull
    val releaseKeyPassword = providers.environmentVariable("FOCUSAI_ANDROID_KEY_PASSWORD").orNull
    val releaseSigningValues = listOf(releaseStoreFile, releaseStorePassword, releaseKeyAlias, releaseKeyPassword)
    require(releaseSigningValues.all { it == null } || releaseSigningValues.all { !it.isNullOrBlank() }) {
        "Configure all FOCUSAI_ANDROID_* signing environment variables or leave all unset."
    }
    val releaseSigning = if (releaseSigningValues.all { !it.isNullOrBlank() }) {
        signingConfigs.create("release") {
            storeFile = file(releaseStoreFile!!)
            storePassword = releaseStorePassword
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
        }
    } else null
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            if (releaseSigning != null) signingConfig = releaseSigning
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
    buildFeatures {
        compose = true
    }
}
