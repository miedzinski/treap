plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spotless)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotlin.reflect)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

spotless {
    kotlin {
        ktlint(libs.versions.ktlint.get())
    }
}
