@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.shadow)
}

tasks.jar {
	manifest.attributes["Main-Class"] = "io.github.aemogie.timble.demo.TimbleKt"
}

dependencies {
	implementation(projects.utils)
	implementation(projects.graphics.gl)
	implementation(projects.ui)
}