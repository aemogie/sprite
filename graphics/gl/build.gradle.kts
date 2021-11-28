dependencies {
	implementation(projects.utils)
	lwjgl("opengl").forEach(::api)
}