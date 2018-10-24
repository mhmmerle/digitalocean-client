import org.apache.tools.ant.taskdefs.Tar

buildscript {
    val mavenResolveUrl: String by project
    val junitPlatformVersion: String by project
    val toolVersioningPluginVersion: String by project

    repositories {
        mavenLocal()
        maven(mavenResolveUrl)
    }

    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
        classpath("com.adcubum.tool:versioning-plugin:$toolVersioningPluginVersion")
    }
}

val mavenResolveUrl: String by project
val mavenReleasePublishUrl: String by project
val mavenSnapshotPublishUrl: String by project

val artifactrepoUsername: String? by project
val artifactrepoPassword: String? by project

val kotlinVersion: String by project
val junitVersion: String by project
val assertJVersion: String by project
val fuelVersion: String by project
val klaxonVersion: String by project
val log4JVersion: String by project
val cliktVersion: String by project

repositories {
    mavenLocal()
    maven(mavenResolveUrl)
}

plugins {
    kotlin("jvm") version "1.3.11"
    application
    `maven-publish`
}

pluginManager.apply("org.junit.platform.gradle.plugin")
pluginManager.apply("com.adcubum.tool.versioning-plugin")

group = "com.adcubum.template"

application {
    mainClassName = "com.adcubum.template.MainKt"
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("com.github.kittinunf.fuel:fuel:$fuelVersion")
    compile("com.beust:klaxon:$klaxonVersion")
    compile("org.apache.logging.log4j:log4j-api:$log4JVersion")
    compile("org.apache.logging.log4j:log4j-core:$log4JVersion")
    compile("com.github.ajalt:clikt:$cliktVersion")

    implementation( "org.jetbrains.kotlin:kotlin-reflect:1.3.11")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")

    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

publishing {
    repositories.maven{
        credentials.username = artifactrepoUsername?:""
        credentials.password = artifactrepoPassword?:""
    }

    publications {
        create<MavenPublication>("applicationTar") {
            from(components["java"])
            artifact(tasks["distTar"])
        }
        create<MavenPublication>("applicationZip") {
            from(components["java"])
            artifact(tasks["distZip"])
        }
    }
}

gradle.taskGraph.whenReady {
    val mavenRepoUri = if (taskGraphContains("release", "candidate", "devSnapshot")) {
        uri(mavenReleasePublishUrl)
    } else {
        uri(mavenSnapshotPublishUrl)
    }
    println("Publishing to maven repository $mavenRepoUri")
    (publishing.repositories.get("maven") as MavenArtifactRepository).url = mavenRepoUri
}

fun taskGraphContains (vararg taskNames : String) : Boolean {
    return gradle.taskGraph.allTasks.any { taskNames.contains(it.name) }
}