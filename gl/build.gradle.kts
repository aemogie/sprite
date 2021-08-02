import org.gradle.internal.os.OperatingSystem

version = "0.1.0-SNAPSHOT-01"

@Suppress("INACCESSIBLE_TYPE")
val lwjglNatives = when (OperatingSystem.current()) {
	OperatingSystem.LINUX   -> System.getProperty("os.arch").let {
		if (it.startsWith("arm") || it.startsWith("aarch64"))
			"natives-linux-${if (it.contains("64") || it.startsWith("armv8")) "arm64" else "arm32"}"
		else
			"natives-linux"
	}
	OperatingSystem.MAC_OS  -> "natives-macos"
	OperatingSystem.WINDOWS -> System.getProperty("os.arch").let {
		if (it.contains("64"))
			"natives-linux${if (it.startsWith("aarch64")) "-arm64" else ""}"
		else
			"natives-linux-x86"
	}
	else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

dependencies {
	api(project(":utils"))
	//region LWJGL
	implementation(platform("org.lwjgl:lwjgl-bom:3.2.3"))
	implementation("org.lwjgl:lwjgl:3.2.3")
	implementation("org.lwjgl:lwjgl-glfw:3.2.3")
	implementation("org.lwjgl:lwjgl-stb:3.2.3")
	implementation("org.lwjgl:lwjgl-opengl:3.2.3")
	implementation("org.lwjgl:lwjgl-openal:3.2.3")
	runtimeOnly("org.lwjgl:lwjgl:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-glfw:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-stb:3.2.3:$lwjglNatives")
	implementation("org.lwjgl:lwjgl-opengl:3.2.3:$lwjglNatives")
	implementation("org.lwjgl:lwjgl-openal:3.2.3:$lwjglNatives")
	//endregion
	implementation("org.joml:joml:1.10.1")
}