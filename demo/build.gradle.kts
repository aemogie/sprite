plugins {
	id("com.github.johnrengelman.shadow") version shadowV
}

tasks.jar {
	manifest.attributes["Main-Class"] = "io.github.aemogie.timble.demo.TimbleKt"
}

dependencies {
	implementation(projects.utils)
	implementation(projects.graphics.gl)
	implementation(projects.ui)
}