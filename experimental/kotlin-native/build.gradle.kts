plugins {
    kotlin("multiplatform") version "1.7.0"
}

group = "com.github.winterreisender"
version = "1.0-SNAP"

repositories {
    mavenCentral()
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
    //js(IR) {
    //    browser {
    //        commonWebpackConfig {
    //            cssSupport.enabled = true
    //        }
    //    }
    //}
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val libwebview by creating {
                    defFile(project.file("src/nativeMain/nativeInterop/cinterop/webview.def"))
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
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
                //implementation(kotlin("test"))
            }
        }
        val jvmTest by getting
        val nativeMain by getting {

        }
        val nativeTest by getting
    }
}


