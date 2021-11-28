dependencies {
	implementation(projects.utils)
	lwjgl("core").forEach(::api)
	lwjgl("assimp").forEach(::api)
	lwjgl("glfw").forEach(::api)
	lwjgl("openal").forEach(::api)
	lwjgl("stb").forEach(::api)
}

subprojects { dependencies { api(project.parent!!) } }