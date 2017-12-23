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
    compile("org.jetbrains.kotlin:kotlin-stdlib:1.2.10")
    compile("org.jsoup:jsoup:1.11.2")
    compile("net.sourceforge.cssparser:cssparser:0.9.24")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.2")
    testCompile("org.assertj:assertj-core:3.8.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.2")
}
