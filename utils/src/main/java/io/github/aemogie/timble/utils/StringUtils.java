package io.github.aemogie.timble.utils;

import java.util.regex.Pattern;

public final class StringUtils {
	private StringUtils() {}
	
	public static final Pattern WORD_REGEX = Pattern.compile("\\w+");
	
	public static String integerWithSpecifiedDigits(int val, int noOfDigits) {
		String valStr = String.valueOf(val);
		if (valStr.length() < noOfDigits) {
			valStr = "0".repeat(noOfDigits - valStr.length()) + valStr;
		}
		return valStr;
	}
}
