version = "0.1.0-SNAPSHOT-01"

val lwjglVersion = "3.2.3"
dependencies {
	api(project(":utils"))
	//region LWJGL
	implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
	implementation("org.lwjgl:lwjgl:$lwjglVersion")
	implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
	implementation("org.lwjgl:lwjgl-stb:$lwjglVersion")
	runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-windows")
	runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = "natives-windows")
	runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = "natives-windows")
	//endregion
	implementation("org.joml:joml:1.10.1")
}