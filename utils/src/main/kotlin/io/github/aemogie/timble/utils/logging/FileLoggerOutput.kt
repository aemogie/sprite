package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import io.github.aemogie.timble.utils.console.STD_ERR
import java.io.*
import kotlin.system.exitProcess

@Suppress("unused") //used via reflection, from the json config
class FileLoggerOutput(config: JsonObject) : LoggerOutput(config) {
	
	private val path: String = config["path"].let {
		if (it == null) {
			STD_ERR.write("Could not locate config value \"path\" for \"${this::class.simpleName}\"!\n".toByteArray())
			STD_ERR.write("Please check your \"${CONFIG_PATH}\"\n".toByteArray())
			exitProcess(-1)
		} else it
	}.asString
	
	private val file: BufferedWriter = BufferedWriter(FileWriter(File(path).apply {
		parentFile.apply { if (!exists()) mkdirs() }
	}))
	
	override fun print(text: String): Boolean {
		return try {
			file.write(text)
			true
		} catch (e: IOException) {
			STD_ERR.write("Unable to write to log file \"$path\"\n".toByteArray())
			STD_ERR.write("${e.message}\n".toByteArray())
			STD_ERR.flush()
			false
		}
	}
	
	override fun destroy(): Boolean {
		return try {
			file.close()
			true
		} catch (e: IOException) {
			STD_ERR.write("Unable to close log file \"$path\"\n".toByteArray())
			STD_ERR.write("${e.message}\n".toByteArray())
			STD_ERR.flush()
			false
		}
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