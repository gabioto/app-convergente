package com.tdp.ms.autogestion.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	public static LocalDateTime formatStringToLocalDateTime(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"); 
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		return dateTime;
	}
}
