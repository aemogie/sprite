import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

private val lwjglNatives = arrayOf(
	"linux",
	"linux-arm64",
	"linux-arm32",
	"macos",
	"macos-arm64",
	"windows",
	"windows-arm64",
	"windows-x86",
)

fun DependencyHandler.lwjgl(dependency: String) = ArrayList<Dependency>().also { dependencies ->
	if (dependency == "core") {
		
		dependencies += lwjglNatives.map { create("org.lwjgl:lwjgl:$lwjglV:natives-$it") }
		dependencies += create("org.lwjgl:lwjgl:$lwjglV")
		dependencies += create(platform("org.lwjgl:lwjgl-bom:$lwjglV"))
		
	} else "org.lwjgl:lwjgl-$dependency:$lwjglV".let { dependencyNotation ->
		
		dependencies += lwjglNatives.map { create("$dependencyNotation:natives-$it") }
		dependencies += create(dependencyNotation)
		
	}
}