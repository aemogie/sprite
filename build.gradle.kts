plugins {
	`java-library`
}

group = "io.github.aemogie.timble"
version = "0.1.0"

subprojects {
	apply(plugin = "java-library")
	
	group = rootProject.group
	version = rootProject.version
	
	repositories {
		mavenCentral()
	}
	
	dependencies {
		testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
	}
	
	tasks.test {
		useJUnitPlatform()
	}
}