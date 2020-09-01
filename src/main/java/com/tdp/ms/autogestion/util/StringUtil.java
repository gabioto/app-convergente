package com.tdp.ms.autogestion.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

	public static String validateEmptyField(String field) {
		return field != null ? field : "";
	}

}
