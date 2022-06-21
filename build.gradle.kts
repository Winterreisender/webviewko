/*
 * Copyright (c) 2022  Winterreisender
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: **Apache-2.0**
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.Winterreisender"
version = "0.1.0"
description = "webviewko"

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "latest.release"
    id("org.jetbrains.dokka") version "1.7.0"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
}

dependencies {
    api("net.java.dev.jna:jna:5.11.0") // can be accessed by users

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")
    dokkaJavadocPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")

    //implementation(kotlin("stdlib"))
    //testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    //dokkaGfmPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs/kdoc"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(rootDir.resolve("docs/javadoc"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    //withJavadocJar()
}

tasks.jar {
    manifest {
        attributes(
            mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version)
        )
    }
}


tasks.shadowJar {
    manifest {
        attributes(mapOf(
            //"Main-Class" to "com.github.winterreisender.webviewko.MainCLIKt",
            "ImplementationTitle" to project.name,
            "Implementation-Version" to project.version)
        )
    }
}



tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
