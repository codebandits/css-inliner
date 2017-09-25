buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    }
}

plugins {
    kotlin("jvm") version "1.1.50"
    `java-gradle-plugin`
    `kotlin-dsl`
//    `maven-publish`
}

task("getCssFilePath") {
    val cssFile = file("$projectDir/src/main/resources/templates/style.css")
    println(cssFile.absolutePath)
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

group = "com.github.codebandits"
version = "1.0"

// TODO instead of src/main/resources/META-INF/gradle-plugins/com.github.codebandits.css-inliner.properties
//gradlePlugin {
//    (plugins) {
//        "CSS Inliner" {
//            id = "com.github.codebandits.css-inliner"
//            implementationClass = "com.github.codebandits.cssinliner.CssInlinerPlugin"
//        }
//    }
//}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile(project(":css-inliner"))
    testImplementation(gradleTestKit())
    testCompile("org.assertj:assertj-core:3.8.0")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.0")
}
