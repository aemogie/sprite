@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.kotlin.serialization)
}

dependencies {
	compileOnly(libs.jetbrains.annotations)
	implementation(libs.kotlinx.serialization.json)
}