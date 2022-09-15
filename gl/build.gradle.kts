dependencies {
	api(projects.graphics)

	implementation(projects.utils)
	with(libs.lwjgl) {
		api(opengl)
		lwjglNatives(opengl)
	}
}