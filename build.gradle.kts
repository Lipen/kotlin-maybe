group = "com.github.Lipen"

plugins {
    kotlin("jvm") version "1.9.22"
    id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))

    // Dependencies
    // ...

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // Test
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

jgitver {
    strategy("MAVEN")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven(layout.buildDirectory.dir("repository"))
    }
}

tasks.wrapper {
    gradleVersion = "8.6"
    distributionType = Wrapper.DistributionType.ALL
}

defaultTasks("clean", "build")
