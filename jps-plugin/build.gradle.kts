//group = "cz.cuni.mff.gitlab.zelezno"
//version = "1.0-SNAPSHOT"


//apply { plugin("java") }
//apply { plugin("org.jetbrains.intellij.platform.module") }

//tasks.test {
//    useJUnitPlatform()
//}

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
//    id("org.jetbrains.intellij.platform") version "2.12.0"
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        targetCompatibility = "21"
    }
}

//plugins {
////    id("java")
////    kotlin("jvm")
//    // maybe just platform.module
////    id("org.jetbrains.intellij.platform.module") version "2.12.0"
//}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
