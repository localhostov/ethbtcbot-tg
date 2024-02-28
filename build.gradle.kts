val ktorVersion: String by project
val kotlinVersion: String by project
val retrofitVersion: String by project
val tgbotVersion: String by project

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.8"
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "lol.hostov"
version = "0.0.1"

application {
    mainClass.set("lol.hostov.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:$tgbotVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}