repositories {
	mavenCentral()
}

dependencies {
	val lwjglNatives: String by rootProject.extra
	implementation(projects.utils)
	
	implementation(platform("org.lwjgl:lwjgl-bom:3.2.3"))
	api("org.lwjgl:lwjgl:3.2.3")
	api("org.lwjgl:lwjgl-assimp:3.2.3")
	api("org.lwjgl:lwjgl-glfw:3.2.3")
	api("org.lwjgl:lwjgl-openal:3.2.3")
	api("org.lwjgl:lwjgl-stb:3.2.3")
	runtimeOnly("org.lwjgl:lwjgl:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-assimp:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-glfw:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-openal:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-stb:3.2.3:$lwjglNatives")
}