buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.10")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.2")
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
