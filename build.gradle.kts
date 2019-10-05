plugins {
    kotlin("jvm") version "1.3.11"
    application
    id("email.haemmerle.baseplugin").version("0.0.5")
}

group = "email.haemmerle.digitalocean.client"
description = "RESTful HTTP Client Library"

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

`email-haemmerle-base` {
    username = "mhmmerle"
}

application {
    mainClassName = "email.haemmerle.digitalocean.client.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.apache.logging.log4j:log4j-api:2.11.1")
    implementation("org.apache.logging.log4j:log4j-core:2.11.1")
    implementation("com.beust:klaxon:5.0.13")
    implementation("com.github.ajalt:clikt:1.5.0")
    implementation( "org.jetbrains.kotlin:kotlin-reflect:1.3.40")
    implementation("email.haemmerle.restclient:lib-rest-client:0.0.1")
    implementation("com.github.ajalt:clikt:2.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testImplementation("org.assertj:assertj-core:3.11.0")
    testImplementation("io.mockk:mockk:1.9")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.2.0")

    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}