dependencies {
	implementation(projects.utils)
	with(libs.lwjgl) {
		api(opengl)
		allLwjglNatives(opengl).forEach(::runtimeOnly)
	}
}