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
version = "0.2.0"
description = "webviewko"

repositories {
    mavenCentral()
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs/kdoc"))
}

lateinit var osPrefix :String

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
        isMingwX64 -> mingwX64("native")
        hostOs == "Linux" -> linuxX64("native")
        hostOs == "Mac OS X" -> macosX64("native") // Not tested
        else -> throw GradleException("$hostOs is not supported.")
    }
    osPrefix = when {
        hostOs == "Linux" -> "linuxX64"
        hostOs == "Mac OS X" -> "macosX64"
        isMingwX64 -> "mingwX64"
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val cwebview by creating {
                    defFile(project.file("src/nativeMain/nativeInterop/cinterop/webview.def"))
                    packageName("${group}.cwebview")
                }
            }

        }
        //binaries {
        //    executable {
        //        entryPoint = "main"
        //        if(hostOs == "Linux") linkerOpts("-Wl,-rpath=${'$'}ORIGIN")
        //
        //        // Copy dll,so to executable file's folder. This does not include debugTest
        //        copy {
        //            from("src/nativeMain/nativeInterop/cinterop/webview/${osPrefix}/")
        //            into(outputDirectory)
        //            into(outputDirectory.toPath().parent.resolve("debugTest"))
        //            include("*.dll", "*.dylib", "*.so")
        //            duplicatesStrategy= DuplicatesStrategy.WARN
        //        }
        //    }
        //}


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
                implementation("net.java.dev.jna:jna-platform:5.12.0")
            }
        }

        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
            }
        }

        val nativeTest by getting
    }

    publishing {
        publications {
            matching {it.name == "native"}.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach {
                        publication.artifactId = "webviewko-${osPrefix}".toLowerCase()
                    }
            }

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
                password = System.getenv("TOKEN")
            }
        }
        maven {
            name = "GitHubPages"
            url = uri("file://${rootDir.resolve("docs/maven-repo")}")
        }
    }
    publications {
        matching {it.name == "native"}.all {
            val targetPublication = this@all
            tasks.withType<AbstractPublishToMaven>()
                .matching { it.publication == targetPublication }
                .configureEach {
                    publication.artifactId = "webviewko-${osPrefix}".toLowerCase()
                }
        }
        //publications {
        //    create<MavenPublication>("maven") {
        //        artifactId = "webviewko-${osPrefix}"
        //        components.forEach { println(it.name) }
        //        from(components["kotlin"])
        //    }
        //}
    }
}

/**
 * TODO: Eliminate all chaos. All lower case, all GitHub Packages, unified name
 * FIXME: Linux can not find .so
 */
