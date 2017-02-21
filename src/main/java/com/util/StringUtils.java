package com.util;

import java.util.Random;

/**
 * @author Vinit Solanki
 *
 */
public class StringUtils {
	
	public static String generateRandomString(int length) {

		return new Random()
				.ints(48, 122)
				.filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
				.mapToObj(i -> (char) i)
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::append,
						StringBuilder::append).toString();
	
	}
	
}
