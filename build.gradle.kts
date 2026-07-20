plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.12.0"
}

group = "cz.cuni.mff.gitlab.zelezno"
version = "1.0"

subprojects {
    apply { plugin("org.jetbrains.intellij.platform.module") }
}

project.apply {
    plugin("org.jetbrains.intellij.platform")
}

allprojects {
    apply { plugin("java") }
    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
    dependencies {
        intellijPlatform {
            intellijIdea("2026.1.1")
            testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

            testImplementation("junit:junit:4.13.2")
            // workaround for <2024.3
            // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#missing-opentest4j-dependency-in-test-framework
            testImplementation("org.opentest4j:opentest4j:1.3.0")
            // Add necessary plugin dependencies for compilation here, example:
            bundledPlugin("com.intellij.java")
//            bundledPlugin("org.jetbrains.kotlin")
//            bundledLibrary("kotlinx-coroutines")
        }
        implementation("com.github.weisj:jsvg:2.0.0")
        implementation("guru.nidi:graphviz-java:0.16.3")
//        implementation("kotlinx-coroutines")
    }
}


dependencies { implementation(project("jps-plugin")) }

// Configure IntelliJ Platform Gradle Plugin

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "261"
        }

//        changeNotes = """
//            Initial version
//        """.trimIndent()
    }
    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        targetCompatibility = "21"
    }
    named("build") {
        dependsOn(":jps-plugin:build")
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
            srcDirs("src/main/java")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
