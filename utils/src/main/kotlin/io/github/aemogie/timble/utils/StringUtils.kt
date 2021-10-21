package io.github.aemogie.timble.utils

import java.util.regex.Pattern

object StringUtils {
	val WORD_REGEX = Pattern.compile("\\w+")
	fun integerWithSpecifiedDigits(`val`: Int, noOfDigits: Int): String {
		var valStr = `val`.toString()
		if (valStr.length < noOfDigits) {
			valStr = "0".repeat(noOfDigits - valStr.length) + valStr
		}	
		return valStr
	}
}