plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.9.7"
}

dependencies {
    compile(project(":css-inliner"))
    testCompile("org.assertj:assertj-core:3.8.0")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.0")
}

data class GradlePlugin(val displayName: String, val id: String, val implementationClass: String)

val plugin = GradlePlugin(
        "CSS Inliner Gradle Plugin",
        "com.github.codebandits.css-inliner",
        "com.github.codebandits.cssinliner.CssInlinerPlugin"
)

gradlePlugin {
    (plugins) {
        plugin.id {
            id = plugin.id
            implementationClass = plugin.implementationClass
        }
    }
}

pluginBundle {
    tags = listOf("CSS", "email")
    website = "https://github.com/codebandits/css-inliner"
    vcsUrl = "https://github.com/codebandits/css-inliner"
    mavenCoordinates.artifactId = base.archivesBaseName
    (plugins) {
        plugin.id {
            id = plugin.id
            displayName = plugin.displayName
            description = plugin.displayName
        }
    }
}

publishing {
    repositories {
        maven(url = "../build/repository") {
            name = "test"
        }
    }
}

tasks {
    val processTestResources: ProcessResources by getting

    val writeTestProperties by creating(WriteProperties::class) {
        outputFile = File(processTestResources.destinationDir, "test.properties")
        property("version", version)
    }

    processTestResources.dependsOn(writeTestProperties)

    val publishDependenciesForTests by creating {
        dependsOn(":css-inliner:publishCssInlinerPublicationToTestRepository")
        dependsOn(":plugin:publishPluginMavenPublicationToTestRepository")
        dependsOn(":plugin:publish${plugin.id.capitalize()}PluginMarkerMavenPublicationToTestRepository")
    }

    "junitPlatformTest" {
        dependsOn(publishDependenciesForTests)
    }
}
