dependencies {
	implementation(projects.utils)
	with(libs.lwjgl) {
		api(core)
		api(assimp)
		api(glfw)
		api(openal)
		api(stb)
		lwjglNatives(core)
		lwjglNatives(assimp)
		lwjglNatives(glfw)
		lwjglNatives(openal)
		lwjglNatives(stb)
	}
}