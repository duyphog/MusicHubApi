package com.aptech.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {

	public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	public static String getCurrentUsername() {
		String currentUser = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!(authentication instanceof AnonymousAuthenticationToken))
			currentUser = authentication.getName();

		return currentUser;
	}

	public static String RandomString(int length) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

	public static Date ParseDateString(final String dateStr) {

		if (dateStr == null) {
			return null;
		}

		SimpleDateFormat format = (dateStr.charAt(4) == '/') ? new SimpleDateFormat("yyyy/MM/dd")
				: new SimpleDateFormat("yyyy-MM-dd");

		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String normalizeUri(String input) {
		return input.trim().replaceAll("[^a-zA-Z0-9]+", "-").toLowerCase();
	}
}
