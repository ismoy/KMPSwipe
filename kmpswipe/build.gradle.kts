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
        version = "1.0.0-alpha-21"
    )
    pom {
        name.set("KmpSwipe")
        description.set(
            "KMPSwipe is a comprehensive Kotlin Multiplatform library designed to seamlessly integrate " +
                    "swipe gestures into your Compose-based applications, targeting both Android and iOS platforms " +
                    "with a unified API. This library empowers developers to add dynamic and interactive " +
                    "swipe functionality to any composable component, be it a Card, Box, or any custom UI element," +
                    " enhancing user experience with intuitive gestures.\n" +
                    "\n" +
                    "Features include:\n" +
                    "\n" +
                    "* **Universal Composable Component Swiping:** Effortlessly enable swipe actions on any UI element within your Compose applications, promoting interactive and engaging user interfaces. This provides flexibility and consistency across your app's UI.\n" +
                    "* **Exhaustive Swipe Behavior Customization:** Precisely adjust swipe thresholds, resistance, animation stiffness, and swipe limit multipliers to achieve the desired swipe behavior. This level of customization allows developers to fine-tune the swipe experience to match specific UI/UX requirements.\n" +
                    "* **Dynamic Swipe Thresholds:** Adapt swipe sensitivity based on dynamic calculations, allowing for a context-aware user experience. This feature ensures that the swipe gesture feels natural and responsive in different scenarios.\n" +
                    "* **Customizable Background Visuals with `SwipeLayout`:** Utilize the `SwipeLayout` component to fully personalize the appearance of swipe backgrounds for left and right swipes, providing clear visual cues to users. This allows for a more branded and visually consistent user experience.\n" +
                    "* **Haptic Feedback Integration with `HapticController`:** Enhance user interaction with optional vibration feedback upon swipe completion, managed by a dedicated controller for better organization and control. This provides tactile feedback, making the swipe action feel more tangible.\n" +
                    "* **Smooth and High-Performance Animations with `AnimationController`:** Leverage Compose's animation capabilities for fluid and responsive swipe gestures, managed by a separate animation controller for advanced optimization and customization. This ensures that swipe animations are smooth and performant, enhancing the overall user experience.\n" +
                    "* **State Management and Persistence with `SwipeController`:** Maintain swipe states across recompositions and handle swipe cancellations gracefully, ensuring UI consistency, all managed within a dedicated swipe controller. This simplifies state management and prevents UI inconsistencies.\n" +
                    "* **Directional Swipe Control:** Specify allowed swipe directions (left, right, or both) to tailor swipe behavior to specific UI requirements. This allows for precise control over which swipe actions are available to the user.\n" +
                    "* **Swipe Velocity Tracking:** Capture swipe velocity for advanced gesture analysis and custom actions, enabling sophisticated interactions. This allows developers to implement velocity-based animations or actions, adding another layer of interactivity.\n" +
                    "* **Comprehensive State Exposure:** Exposes various states like Swiping, End, and Cancelled, allowing developers to build complex state-dependent UIs. This provides developers with granular control over the UI based on the swipe state.\n" +
                    "* **Unified API for Cross-Platform Development:** Streamline cross-platform development with a consistent API for Android and iOS, reducing platform-specific code. This simplifies the development process and ensures consistency across platforms.\n" +
                    "\n" +
                    "Integrate dynamic, interactive, and highly customizable swipe actions into your Kotlin Multiplatform projects with KMPSwipe, elevating your mobile application's UI/UX to a new level of sophistication and performance.\n" +
                    "\n" +
                    "Tags: #KMP #KotlinMultiplatform #Swipe #JetpackCompose #ComposeUI #Android #iOS #Gesture #MobileDevelopment #UI #UX #Composable #Library #OpenSource #Animation #HapticFeedback #StateManagement #CrossPlatform #Controllers #Modular"
        )
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
