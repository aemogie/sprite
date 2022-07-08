import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider

private val nativeClassifiers = arrayOf(
	"natives-linux",
	"natives-linux-arm64",
	"natives-linux-arm32",
	"natives-macos",
	"natives-macos-arm64",
	"natives-windows",
	"natives-windows-arm64",
	"natives-windows-x86",
)

fun DependencyHandler.allLwjglNatives(
	dependency: Provider<MinimalExternalModuleDependency>
) = nativeClassifiers.map { native ->
	variantOf(dependency) { it.classifier(native) }
}