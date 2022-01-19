package io.github.aemogie.timble.utils

import java.io.FileOutputStream
import kotlin.system.exitProcess

//maybe: remove this in a way where its still clean.
inline fun <T> T?.exitIfNull(exitCode: Int = -1, exitAction: () -> Unit = {}) = this ?: run {
	exitAction()
	exitProcess(exitCode)
}

//maybe: this as well. not just inline. inline where its clean.
fun FileOutputStream.println(content: String) = write("$content${System.lineSeparator()}".toByteArray())