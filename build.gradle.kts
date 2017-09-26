buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.1.50")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("maven-publish")
        plugin("org.junit.platform.gradle.plugin")
    }

    repositories {
        mavenCentral()
    }
}
