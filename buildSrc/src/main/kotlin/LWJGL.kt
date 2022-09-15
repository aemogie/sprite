import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem

object LWJGL {
	var allNatives = true

	var current = Pair(
		OperatingSystem.current(), System.getProperty("os.arch")
	).let { (os, arch) ->
		when {
			os.isMacOsX -> if (arch.startsWith("aarch64")) "natives-macos-arm64" else "natives-macos"
			os.isLinux || (os.isUnix && !os.isMacOsX) -> { // linux, solaris and freebsd
				if (arch.startsWith("arm") || arch.startsWith("aarch64")) {
					if (arch.contains("64") || arch.startsWith("armv8")) "natives-linux-arm64"
					else "natives-linux-arm32"
				} else "natives-linux"
			}
			os.isWindows -> {
				if (arch.contains("64")) {
					if (arch.startsWith("aarch64")) "natives-windows-arm64" else "natives-windows"
				} else "natives-windows-x86"
			}
			else -> error("Unrecognized or unsupported platform. Please set \"LWJGL.current\" manually")
		}
	}

	internal val classifiers = arrayOf(
		"natives-linux",
		"natives-linux-arm64",
		"natives-linux-arm32",
		"natives-macos",
		"natives-macos-arm64",
		"natives-windows",
		"natives-windows-arm64",
		"natives-windows-x86",
	)
}

fun DependencyHandler.lwjglNatives(dependency: Provider<MinimalExternalModuleDependency>) {
	if (LWJGL.allNatives) {
		LWJGL.classifiers.map { native ->
			variantOf(dependency) { it.classifier(native) }
		}.forEach { add("runtimeOnly", it) }
	} else {
		add("runtimeOnly", variantOf(dependency) { it.classifier(LWJGL.current) })
	}
}