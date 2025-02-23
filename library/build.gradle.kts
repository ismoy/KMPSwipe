import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.compose.compiler)
    `maven-publish`
}

group = "io.github.ismoy.tailorswipe"
version = "1.0.1"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvm("desktop"){
        compilations.all{
            kotlinOptions.jvmTarget = "11"
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()
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
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val androidMain by getting {
        }
        val desktopMain by getting {
            dependencies{
            }
        }
    }
}

android {
    namespace = "io.github.ismoy.tailorswipe"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildTypes{
        release {  }
        debug {  }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
publishing{
    repositories {
        mavenLocal()
    }
}

/*mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "My library"
        description = "A library."
        inceptionYear = "2024"
        url = "https://github.com/kotlin/multiplatform-library-template/"
        licenses {
            license {
                name = "XXX"
                url = "YYY"
                distribution = "ZZZ"
            }
        }
        developers {
            developer {
                id = "XXX"
                name = "YYY"
                url = "ZZZ"
            }
        }
        scm {
            url = "XXX"
            connection = "YYY"
            developerConnection = "ZZZ"
        }
    }
}*/
