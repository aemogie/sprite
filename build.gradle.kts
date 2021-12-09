plugins {
	kotlin("jvm") version kotlinV
}

tasks.wrapper {
	gradleVersion = gradleV
	distributionType = Wrapper.DistributionType.ALL
}

allprojects {
	apply {
		plugin("java-library")
		plugin("org.jetbrains.kotlin.jvm")
	}

	group = "io.github.aemogie.timble"
	version = projectV

	java {
		sourceCompatibility = javaV
		targetCompatibility = javaV
		withSourcesJar()
		withJavadocJar()
	}

	kotlin.target.compilations.forEach {
		it.kotlinOptions.jvmTarget = javaV.toString()
	}

	repositories { mavenCentral() }

	dependencies {
		implementation(kotlin("stdlib-jdk8"))
		implementation(kotlin("reflect"))
	}
}