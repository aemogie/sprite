package io.github.aemogie.timble.utils.logging

import com.google.gson.JsonObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Paths
import kotlin.system.exitProcess

class FileLoggerOutput(logLevel: Logger.Level, pattern: String, config: JsonObject?) :
	LoggerOutput(logLevel, pattern, config) {
	private val path: String
	private val file: FileWriter?
	override fun print(msg: Array<String?>): Boolean {
		return try {
			for (s in msg) file!!.write(s)
			file!!.flush()
			true
		} catch (e: IOException) {
			Logger.SYS_ERR.println("Unable to write to log file.")
			false
		}
	}
	
	override fun colourise(out: Array<String?>, level: Logger.Level): Array<String?> {
		return out
	}
	
	override fun destroy(): Boolean {
		return try {
			file!!.close()
			true
		} catch (e: IOException) {
			Logger.SYS_ERR.println("Unable to close log file - " + Paths.get(path).toAbsolutePath())
			false
		}
	}
	
	init {
		if (this.config == null) {
			Logger.SYS_ERR.printf(
				"Could not locate config for \"%s\"! Please check your \"META-INF/timble-logger.json\"%n",
				javaClass.simpleName
			)
			exitProcess(-1)
		}
		val configPath = this.config["path"]
		if (configPath == null) {
			Logger.SYS_ERR.printf(
				"Could not locate config value \"path\" for \"%s\"! Please check your \"META-INF/timble-logger.json\"%n",
				javaClass.simpleName
			)
			exitProcess(-1)
		}
		path = configPath.asString
		var tempFile: FileWriter? = null
		try {
			tempFile = FileWriter(path)
		} catch (e: IOException) {
			val i = path.lastIndexOf('/') + 1
			val directory = File(path.substring(0, i))
			if (!directory.exists() && directory.mkdir()) {
				try {
					tempFile = FileWriter(path)
				} catch (ioException: IOException) {
					Logger.SYS_ERR.println(
						"Could not create log file at: " + Paths.get(path).toAbsolutePath()
					)
				}
			}
		}
		file = tempFile
	}
}