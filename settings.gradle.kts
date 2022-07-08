rootProject.name = "timble"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage") repositories { mavenCentral() }
}

include("utils", "graphics", "ui")
include("graphics:gl")
include("demo")