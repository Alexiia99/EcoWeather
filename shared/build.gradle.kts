plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    cocoapods {
        summary = "LoL Weather Shared Module"
        homepage = "Link to a Kotlin/Native module homepage"
        version = "1.0"

        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.4")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("io.insert-koin:koin-core:3.5.0")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.3.4")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.4")
            }
        }
    }
}

android {
    namespace = "com.lolweather.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}