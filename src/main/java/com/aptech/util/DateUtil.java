package com.aptech.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

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

	public static Long GetCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

}
