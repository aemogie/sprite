rootProject.name = "timble"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include("utils", "graphics", "ui", "engine")
graphicsApi("gl")

fun graphicsApi(vararg projectPaths: String?) = include(*projectPaths.map { "graphics:$it" }.toTypedArray())