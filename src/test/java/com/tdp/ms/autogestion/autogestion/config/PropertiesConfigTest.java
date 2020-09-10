package com.tdp.ms.autogestion.autogestion.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.tdp.ms.autogestion.config.PropertiesConfig;

@ExtendWith(MockitoExtension.class)
public class PropertiesConfigTest {

	@Autowired
	private PropertiesConfig config;

	@Test
	void getOAuthUrl() {
//		when(config.getOAuthClient()).thenReturn("");
//		String oAuthClient = config.getOAuthClient();
//		assertEquals("", oAuthClient);
	}
}
