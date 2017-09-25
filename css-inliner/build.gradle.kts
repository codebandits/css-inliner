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
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile("org.jsoup:jsoup:1.10.3")
    compile("net.sourceforge.cssparser:cssparser:0.9.23")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0")
    testCompile("org.assertj:assertj-core:3.8.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.0")
}
