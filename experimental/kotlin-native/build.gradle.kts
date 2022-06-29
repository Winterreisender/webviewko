plugins {
    kotlin("multiplatform") version "1.7.0"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.7.0"
}

group = "com.github.winterreisender"
version = "0.2.0"

repositories {
    mavenCentral()
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")
    dokkaJavadocPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.0")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }

    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        isMingwX64 -> mingwX64("mingwX64")
        //hostOs == "Linux" -> linuxX64("native")
        //hostOs == "Mac OS X" -> macosX64("native")
        else -> throw GradleException("$hostOs is not supported.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val libwebview by creating {
                    defFile(project.file("src/mingwX64Main/nativeInterop/cinterop/webview.def"))
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
        val mingwX64Main by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
            }
        }
        val mingwX64Test by getting
    }
}

/*
publishing {
    repositories {
        maven {

        }
    }
}
*/
