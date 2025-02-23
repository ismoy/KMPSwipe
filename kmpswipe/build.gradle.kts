import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish") version "0.30.0"
    `maven-publish`
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "kmpswipe"
            isStatic = true
        }
        target.mavenPublication {}
    }
    withSourcesJar(true)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.animation)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
            }
        }

        val androidMain by getting {}
        val iosMain by getting {}
        all {
            languageSettings {
                optIn("kotlin.ExperimentalMultiplatform")
                optIn("kotlin.ExperimentalUnsignedTypes")
            }
        }
    }
    metadata {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += "-Xexport-kdoc"
            }
        }
    }
}

android {
    namespace = "io.github.ismoy.kmpswipe"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}
mavenPublishing{
    coordinates(
        groupId = "io.github.ismoy",
        artifactId = "kmpswipe",
        version = "1.0.0-alpha-1"
    )
    pom {
        name.set("KmpSwipe")
        description.set("KMPSwipe is a versatile Kotlin Multiplatform library that enables swipe " +
                "gestures on any composable component (Card, Box, etc.). It provides a customizable" +
                " and smooth swipe experience for both Android and iOS, with a unified API. Features" +
                " include:\n" +
                "\n" +
                "* **Universal Composable Swiping:** Easily add swipe functionality to any UI " +
                "element within your Compose-based applications.\n" +
                "* **Highly Customizable:** Control swipe thresholds, resistance, animation " +
                "stiffness, and background visuals for left and right swipes.\n" +
                "* **Haptic Feedback:** Enhance user experience with optional vibration " +
                "feedback upon swipe completion.\n" +
                "* **Smooth Animations:** Utilizes Compose's animation capabilities " +
                "for fluid and responsive swipe gestures.\n" +
                "* **Unified API:** Simplifies cross-platform development with a " +
                "consistent API for Android and iOS.\n" +
                "\n" +
                "Integrate dynamic and interactive swipe actions into your Kotlin " +
                "Multiplatform projects with KMPSwipe.\n" +
                "\n" +
                "Tags: #KMP #KotlinMultiplatform #Swipe #JetpackCompose #ComposeUI " +
                "#Android #iOS #Gesture #MobileDevelopment #UI #UX #Composable #Library #OpenSource")
        inceptionYear.set("2025")
        url.set("https://github.com/ismoy/KMPSwipe")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/ismoy/KMPSwipe/blob/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("ismoy")
                name.set("Ismoy Belizaire")
                email.set("belizairesmoy72@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/ismoy/KMPSwipe")
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
afterEvaluate {
    publishing {
        publications.forEach { publication ->
            val mavenPublication = publication as? MavenPublication
            if (mavenPublication != null) {
                if (mavenPublication.name == "kotlinMultiplatform") {
                    mavenPublication.artifactId = "kmpswipe"
                } else {
                    println("Leaving platform-specific artifactId: ${mavenPublication.artifactId}")
                }

                println("Configured publication: ${mavenPublication.name}, artifactId: ${mavenPublication.artifactId}")
            }
        }
    }
}
