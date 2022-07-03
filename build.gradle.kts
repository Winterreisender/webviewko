/*
 * Copyright (C) 2022. Winterreisender
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: Apache-2.0
 */

plugins {
    kotlin("multiplatform") version "1.7.0"
    id("maven-publish")
    `java-library`
    id("org.jetbrains.dokka") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "latest.release"
}

group = "com.github.winterreisender"
version = "0.2.0-SNAPSHOT"
description = "webviewko"

repositories {
    mavenCentral()
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")
    dokkaJavadocPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs/kdoc"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(rootDir.resolve("docs/javadoc"))
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        java {
            withSourcesJar()
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        isMingwX64 -> mingwX64("mingwX64")
        hostOs == "Linux" -> linuxX64("linuxX64")
        //hostOs == "Mac OS X" -> macosX64("native")
        else -> throw GradleException("$hostOs is not supported.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val libwebview by creating {
                    val osPrefix = when {
                        hostOs == "Linux" -> "linuxX64"
                        isMingwX64 -> "mingwX64"
                        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
                    }
                    defFile(project.file("src/${osPrefix}Main/nativeInterop/cinterop/webview.def"))
                    // There is a cinteropLibwebviewDllNative in Gradle, still don't know how to use.
                    //copy {
                    //    from("src/nativeMain/nativeInterop/cinterop/webview/*.dll")
                    //    into(buildDir.resolve("bin/native/debugExecutable/"))
                    //    into(buildDir.resolve("bin/native/debugTest/"))
                    //    into(buildDir.resolve("bin/native/releaseExecutable"))
                    //}
                }
            }

        }
        binaries {
            executable {
                entryPoint = "main"
                if(hostOs == "Linux") linkerOpts("-Wl,-rpath=.")
                //linkerOpts("-v")
                //linkerOpts("-lole32", "-lshell32", "-lshlwapi", "-luser32") //"-lWebView2Loader.dll")
            }
            // The best way for static link is to wait for Kotlin/Native to upgrade their Mingw (9.0.0) to a newer version
        }


    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
            }
            tasks.dokkaHtml.configure {
                outputDirectory.set(rootDir.resolve("docs/kdoc"))
            }

            tasks.dokkaJavadoc.configure {
                outputDirectory.set(rootDir.resolve("docs/javadoc"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                api("net.java.dev.jna:jna:5.12.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                api("net.java.dev.jna:jna-platform:5.12.0")
            }
        }

        if(isMingwX64) {
            val mingwX64Main by getting {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
                }
            }

            val mingwX64Test by getting
        }

        if(hostOs == "Linux") {
            val linuxX64Main by getting {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
                }
            }
            val linuxX64Test by getting
        }
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("all")
    val main by kotlin.jvm().compilations
    from(main.output)
    configurations += main.compileDependencyFiles as Configuration
    configurations += main.runtimeDependencyFiles as Configuration

    manifest {
        attributes(mapOf(
            "ImplementationTitle" to project.name,
            "Implementation-Version" to project.version)
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Winterreisender/webviewko")
            credentials {
                username =  System.getenv("USERNAME")
                password = System.getenv("TOKEN") //project.findProperty("gpr.key") as String? ?:
            }
        }
    }
    publications {

    }
}


