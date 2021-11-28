plugins {
	id("com.github.johnrengelman.shadow") version "7.0.0"
}

tasks.jar {
	manifest.attributes["Main-Class"] = "io.github.aemogie.timble.engine.TimbleKt"
}

dependencies {
	implementation(projects.utils)
	implementation(projects.graphics.gl)
	implementation(projects.ui)
}