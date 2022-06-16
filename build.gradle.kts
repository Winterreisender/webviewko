import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.test.webviewko"
version = "1.0-SNAPSHOT"
description = "webviewko"


plugins {
    java
    kotlin("jvm") version "1.7.0"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.4")
    testImplementation(kotlin("test"))
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("net.java.dev.jna:jna:5.11.0")

}

tasks {
    test {
        useJUnitPlatform()
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}