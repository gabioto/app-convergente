package com.tdp.ms.autogestion.autogestion.repository.datasource.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.config.PropertiesConfig;
import com.tdp.ms.autogestion.exception.ExternalServerException;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.repository.datasource.api.OAuthApi;
import com.tdp.ms.autogestion.repository.datasource.api.entities.OAuthApiResponse;

@ExtendWith(MockitoExtension.class)
public class OAuthApiTest {

	@InjectMocks
	private OAuthApi oAuthApi;

	@Mock
	private PropertiesConfig config;

	@Mock
	private HttpComponentsClientHttpRequestFactory initClientRestTemplate;

	@Mock
	private RestTemplate restTemplate;

	private static Map<String, OAuthApiResponse> oAuthApiResponseMap = new HashMap<>();
	private static Map<String, OAuth> oAuthRequestMap = new HashMap<>();
	private final String fakeUrl = "";

	@BeforeAll
	public static void setup() throws JsonProcessingException {
		// TICKET API RESPONSE
		oAuthApiResponseMap.put("success_oauth", new OAuthApiResponse("1", "PARAM_KEY_OAUTH_TOKEN", "Bearer", "",
				"3600", "1599178967", "scope1", "", "2682000"));
		oAuthApiResponseMap.put("empty_oauth", null);

		// TICKET REQUEST
		oAuthRequestMap.put("last_oauth",
				new OAuth("PARAM_KEY_OAUTH_TOKEN", "Bearer", "", "3600", "1599175367", "scope1", "", "2682000"));

	}

	@Test
	void generateTicket_responseOk() throws HttpClientErrorException, Exception {
		when(config.getOAuthUrl()).thenReturn(fakeUrl);

		when(config.getOAuthClient()).thenReturn("252525");

		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(),
				ArgumentMatchers.<Class<OAuthApiResponse>>any())).thenReturn(
						new ResponseEntity<OAuthApiResponse>(oAuthApiResponseMap.get("success_oauth"), HttpStatus.OK));

		OAuth oAuth = oAuthApi.generate(oAuthRequestMap.get("last_oauth"));

		assertNotNull(oAuth);
	}

	@Test
	void generateTicket_responseNull() {
		when(config.getOAuthUrl()).thenReturn(fakeUrl);

		when(config.getOAuthClient()).thenReturn("252525");

		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(),
				ArgumentMatchers.<Class<OAuthApiResponse>>any())).thenReturn(
						new ResponseEntity<OAuthApiResponse>(oAuthApiResponseMap.get("empty_oauth"), HttpStatus.OK));

		ExternalServerException exception = assertThrows(ExternalServerException.class, () -> {
			oAuthApi.generate(oAuthRequestMap.get("last_oauth"));
		});

		assertEquals(exception.getMessage(), "500 Error when get OAuth response");
	}

}
