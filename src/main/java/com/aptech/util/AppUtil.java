package com.aptech.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtil {
	
	public static DateFormat  dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static Date getNow() {
		return new Date(System.currentTimeMillis());
	}
	
}
