import org.gradle.internal.os.OperatingSystem

group = "io.github.aemogie.timble"
version = "0.1.0"

// Wish I could move this
extra.apply {
	@Suppress("INACCESSIBLE_TYPE")
	set("lwjglNatives",
		when (OperatingSystem.current()) {
			OperatingSystem.LINUX -> System.getProperty("os.arch").let {
				if (it.startsWith("arm") || it.startsWith("aarch64")) "natives-linux-${
					if (it.contains("64") || it.startsWith(
							"armv8"
						)
					) "arm64" else "arm32"
				}"
				else "natives-linux"
			}
			OperatingSystem.MAC_OS -> "natives-macos"
			OperatingSystem.WINDOWS -> System.getProperty("os.arch").let {
				if (it.contains("64")) "natives-windows${if (it.startsWith("aarch64")) "-arm64" else ""}"
				else "natives-windows-x86"
			}
			else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
		})
}

subprojects {
	apply(plugin = "java-library")
	
	group = rootProject.group
	version = rootProject.version
	
	repositories {
		mavenCentral()
	}
}