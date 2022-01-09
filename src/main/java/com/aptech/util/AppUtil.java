package com.aptech.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtil {
	
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

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(length)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    
	    return generatedString;
	}
	
}
