publishing {
    publications.create<MavenPublication>("cssInliner") {
        from(components["java"])
    }
    repositories {
        maven(url = "../build/repository") {
            name = "test"
        }
    }
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib:1.1.50")
    compile("org.jsoup:jsoup:1.10.3")
    compile("net.sourceforge.cssparser:cssparser:0.9.23")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0")
    testCompile("org.assertj:assertj-core:3.8.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.0")
}
