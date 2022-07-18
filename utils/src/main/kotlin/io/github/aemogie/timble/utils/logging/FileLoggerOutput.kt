package io.github.aemogie.timble.utils.logging

import io.github.aemogie.timble.utils.console.STD_ERR
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.system.exitProcess

@Suppress("unused") //used via reflection, from the json config
class FileLoggerOutput(config: JsonObject) : LoggerOutput(config) {

	private val path: String = config["path"].let {
		if (it == null) {
			STD_ERR.write("Could not locate config value \"path\" for \"${this::class.simpleName}\"!\n".toByteArray())
			STD_ERR.write("Please check your \"${CONFIG_PATH}\"\n".toByteArray())
			exitProcess(-1)
		} else it
	}.jsonPrimitive.content

	private val file: BufferedWriter = BufferedWriter(FileWriter(File(path).apply {
		parentFile.apply { if (!exists()) mkdirs() }
	}))

	override fun print(text: String) = try {
		file.write(text)
	} catch (e: IOException) {
		STD_ERR.write("Unable to write to log file \"$path\"\n".toByteArray())
		STD_ERR.write("${e.message}\n".toByteArray())
		STD_ERR.flush()
	}

	override fun destroy() = try {
		file.close()
	} catch (e: IOException) {
		STD_ERR.write("Unable to close log file \"$path\"\n".toByteArray())
		STD_ERR.write("${e.message}\n".toByteArray())
		STD_ERR.flush()
	}

	override fun format(record: LogRecord, current: String): String = record.instant.toEpochMilli().let {
		"[%tH:%tM:%tS] [%5s|%5s] [%s] %s%n".format(
			it, it, it,
			record.thread.name,
			record.level,
			record.caller.className,
			current
		)
	}
}