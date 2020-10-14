package com.tdp.ms.autogestion.repository.datasource.api;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.tdp.ms.autogestion.config.PropertiesConfig;
import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.ExternalServerException;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.repository.datasource.api.entities.OAuthApiResponse;

@Component
public class OAuthApi {

	private static final Log log = LogFactory.getLog(OAuthApi.class);
	private static final String TAG = OAuthApi.class.getCanonicalName();

	@Autowired
	private PropertiesConfig config;

	@Autowired
	private HttpComponentsClientHttpRequestFactory initClientRestTemplate;

	//@Autowired
	//private RestTemplate restTemplate;

	public OAuth generate(OAuth pOAuth) throws Exception, HttpClientErrorException {
		//restTemplate.setRequestFactory(initClientRestTemplate);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		String requestUrl = config.getOAuthUrl();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("client_id", config.getOAuthClient());
		paramsMap.add("grant_type", "refresh_token");
		paramsMap.add("scope", "scope1");
		paramsMap.add("refresh_token", pOAuth.getRefreshToken());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramsMap, headers);

		log.info(TAG + " " + new Gson().toJson(entity));

		try {
			ResponseEntity<OAuthApiResponse> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity,
					OAuthApiResponse.class);

			OAuthApiResponse authResponse = response.getBody();

			log.info(new Gson().toJson(authResponse));

			if (authResponse != null && response.getStatusCode().equals(HttpStatus.OK)) {
				authResponse.setConsentedOn(String.valueOf(new Date().getTime() / 1000));
				return authResponse.fromThis();
			} else {
				throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when get OAuth response");
			}
		} catch (HttpClientErrorException e) {
			log.info(TAG + e.getMessage());
			log.info(TAG + e.getResponseBodyAsString());
			throw new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, e.getLocalizedMessage());
		} catch (Exception e) {
			log.info(TAG + e.getMessage());
			throw e;
		}
	}
}
