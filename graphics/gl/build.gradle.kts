dependencies {
	val lwjglNatives: String by parent!!.extra
	implementation(projects.utils)
	api("org.lwjgl:lwjgl-opengl:3.2.3")
	runtimeOnly("org.lwjgl:lwjgl-opengl:3.2.3:$lwjglNatives")
}