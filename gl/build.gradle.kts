import org.gradle.internal.os.OperatingSystem

@Suppress("INACCESSIBLE_TYPE")
val lwjglNatives = when (OperatingSystem.current()) {
	OperatingSystem.LINUX -> System.getProperty("os.arch").let {
		if (it.startsWith("arm") || it.startsWith("aarch64"))
			"natives-linux-${if (it.contains("64") || it.startsWith("armv8")) "arm64" else "arm32"}"
		else
			"natives-linux"
	}
	OperatingSystem.MAC_OS -> "natives-macos"
	OperatingSystem.WINDOWS -> System.getProperty("os.arch").let {
		if (it.contains("64"))
			"natives-windows${if (it.startsWith("aarch64")) "-arm64" else ""}"
		else
			"natives-windows-x86"
	}
	else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":utils"))
	
	implementation(platform("org.lwjgl:lwjgl-bom:3.2.3"))
	api("org.lwjgl:lwjgl:3.2.3")
	api("org.lwjgl:lwjgl-assimp:3.2.3")
	api("org.lwjgl:lwjgl-glfw:3.2.3")
	api("org.lwjgl:lwjgl-openal:3.2.3")
	api("org.lwjgl:lwjgl-opengl:3.2.3")
	api("org.lwjgl:lwjgl-stb:3.2.3")
	runtimeOnly("org.lwjgl:lwjgl:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-assimp:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-glfw:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-openal:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-opengl:3.2.3:$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-stb:3.2.3:$lwjglNatives")
}