plugins {
	`java-library`
}

group = "io.github.aemogie.timble"

subprojects {
	apply {
		plugin("java-library")
	}
	
	group = rootProject.group
	println(name)
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