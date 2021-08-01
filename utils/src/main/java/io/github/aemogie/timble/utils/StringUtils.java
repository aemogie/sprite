package io.github.aemogie.timble.utils;

public class StringUtils {
	public static String integerWithSpecifiedDigits(int val, int noOfDigits) {
		String valStr = String.valueOf(val);
		if (valStr.length() < noOfDigits) {
			valStr = "0".repeat(noOfDigits - valStr.length()) + valStr;
		}
		return valStr;
	}
}
