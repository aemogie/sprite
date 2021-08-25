dependencies {
	val lwjglNatives: String by rootProject.extra
	api(projects.graphics)
	implementation(projects.utils)
	api("org.lwjgl:lwjgl-opengl:3.2.3")
	runtimeOnly("org.lwjgl:lwjgl-opengl:3.2.3:$lwjglNatives")
}