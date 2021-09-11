group = "io.github.aemogie.timble"
version = "0.1.0"

subprojects {
	apply(plugin = "java-library")
	
	group = rootProject.group
	version = rootProject.version
	
	repositories { mavenCentral() }
}