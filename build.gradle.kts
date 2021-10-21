import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins { kotlin("jvm") version "1.5.31" }

val javaVersion = JavaVersion.VERSION_16

tasks.wrapper {
	gradleVersion = "7.2"
	distributionType = ALL
}

allprojects {
	apply {
		plugin(JavaLibraryPlugin::class)
		plugin(KotlinPluginWrapper::class)
	}
	
	group = "io.github.aemogie.timble"
	version = "0.1.0"
	
	java {
		sourceCompatibility = javaVersion
		targetCompatibility = javaVersion
		withSourcesJar()
		withJavadocJar()
	}
	
	kotlin.target.compilations.forEach {
		it.kotlinOptions.jvmTarget = javaVersion.toString()
	}
	
	repositories { mavenCentral() }
	
	dependencies {
		implementation(kotlin("stdlib-jdk8"))
		implementation(kotlin("reflect"))
	}
}