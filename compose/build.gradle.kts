import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("maven-publish")
}

group = "com.github.winterreisender"
version = "0.6.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://gitlab.com/api/v4/projects/38224197/packages/maven")
}

kotlin {
    //jvm("awt") {}
    jvm("compose") {}
    sourceSets {
        val composeMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                api("com.github.winterreisender:webviewko-jvm:0.6.0-SNAPSHOT")
            }
        }
        val composeTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    publishing {
    }
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
            name = "GitLabPackages"
            url = uri("https://gitlab.com/api/v4/projects/38224197/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Deploy-Token"//name =  "Private-Token"
                value = System.getenv("GITLAB_DEPLOY_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }

    }
    publications {
    }
}

