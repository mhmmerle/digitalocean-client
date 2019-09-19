buildscript {
    val junitPlatformVersion: String by project

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
    }
}

val kotlinVersion: String by project
val junitVersion: String by project
val assertJVersion: String by project
val fuelVersion: String by project
val klaxonVersion: String by project
val log4JVersion: String by project
val cliktVersion: String by project

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

plugins {
    kotlin("jvm") version "1.3.11"
    application
}

pluginManager.apply("org.junit.platform.gradle.plugin")

group = "email.haemmerle.digitalocean.client"

application {
    mainClassName = "email.haemmerle.digitalocean.client.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.beust:klaxon:$klaxonVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4JVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4JVersion")
    implementation("com.github.ajalt:clikt:$cliktVersion")

    implementation( "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("io.mockk:mockk:1.9")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.2.0")

    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

}