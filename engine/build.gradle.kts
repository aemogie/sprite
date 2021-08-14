plugins {
	id("com.github.johnrengelman.shadow") version "7.0.0"
}

tasks.jar {
	manifest.attributes["Main-Class"] = "io.github.aemogie.timble.engine.Timble"
}

dependencies {
	implementation(project(":utils"))
	implementation(project(":gl"))
	implementation(project(":ui"))
}