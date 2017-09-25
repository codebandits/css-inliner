tasks {

    val plugin by creating(GradleBuild::class) {
        dir = file("plugin")
        tasks = listOf("publish")
    }

    val sample by creating(GradleBuild::class) {
        dir = file("sample")
        tasks = listOf("processResources")
    }

    sample.dependsOn(plugin)
}
