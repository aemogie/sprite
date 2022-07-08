dependencies {
	implementation(projects.utils)
	with(libs.lwjgl) {
		api(core)
		api(assimp)
		api(glfw)
		api(openal)
		api(stb)
		allLwjglNatives(core).forEach(::runtimeOnly)
		allLwjglNatives(assimp).forEach(::runtimeOnly)
		allLwjglNatives(glfw).forEach(::runtimeOnly)
		allLwjglNatives(openal).forEach(::runtimeOnly)
		allLwjglNatives(stb).forEach(::runtimeOnly)
	}
}

project.also { parent ->
	subprojects { dependencies { api(parent) } }
}