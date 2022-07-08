@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.kotlin.jvm)
}

tasks.wrapper {
	gradleVersion = "7.4.2"
	distributionType = Wrapper.DistributionType.ALL
}

subprojects {
	apply {
		plugin("java-library") //api() configuration for gradle
		plugin(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
	}

	group = "io.github.aemogie.timble"
	version = "0.1.0"

	java {
		withSourcesJar()
		withJavadocJar()
	}

	dependencies {
		with(rootProject.libs.kotlin) {
			implementation(stdlib.jdk8)
			implementation(reflect)
		}
	}
}