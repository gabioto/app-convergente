package com.tdp.ms.autogestion.autogestion.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.tdp.ms.autogestion.util.StringUtil;

public class StringUtilTest {

	@Test
	void validateNullField() {
		String response = StringUtil.validateEmptyField(null);
		
		assertNotNull(response);
		assertEquals("", response);
	}
	
	@Test
	void validateValueField() {
		String response = StringUtil.validateEmptyField("something");
		
		assertNotNull(response);
		assertEquals("something", response);
	}
}
