package io.github.aemogie.timble.utils.logging

object LogManager {
	private var logger: Logger? = null
	private fun createLogger(): Logger? {
		logger = Logger()
		return logger
	}
	
	@JvmStatic fun nullifyLogger(): Boolean {
		if (logger!!.destroy()) {
			logger = null
			return true
		}
		return false
	}
	
	@JvmStatic fun getLogger(): Logger? {
		return if (logger == null) createLogger() else logger
	}
}