package com.tdp.ms.autogestion.autogestion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.web.client.HttpClientErrorException;

import com.tdp.ms.autogestion.exception.GenericDomainException;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.repository.OAuthRepositoryImpl;
import com.tdp.ms.autogestion.repository.datasource.api.OAuthApi;
import com.tdp.ms.autogestion.repository.datasource.api.entities.OAuthApiResponse;
import com.tdp.ms.autogestion.repository.datasource.db.JpaOAuthRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblOauth;

@ExtendWith(MockitoExtension.class)
public class OAuthRepositoryTest {

	@InjectMocks
	private OAuthRepositoryImpl oAuthRepository;

	@Mock
	private OAuthApi oAuthApi;

	@Mock
	private JpaOAuthRepository jpaOAuthRepository;

	private static Map<String, TblOauth> ticketRequestMap = new HashMap<>();
	private static Map<String, OAuthApiResponse> oAuthApiResponseMap = new HashMap<>();

	@BeforeAll
	public static void setup() {
		Long actualDate = LocalDateTime.now().toInstant(ZoneOffset.of("-05:00")).toEpochMilli();
		Long seconds = actualDate / 1000;

		oAuthApiResponseMap.put("success_oauth", new OAuthApiResponse("1", "PARAM_KEY_OAUTH_TOKEN", "Bearer", "",
				"3600", "1599178967", "scope1", "", "2682000"));

		TblOauth tblOAuth = new TblOauth();
		tblOAuth.setAccessToken("");
		tblOAuth.setConsentedOn(String.valueOf(seconds));
		tblOAuth.setExpiresIn("3600");
		tblOAuth.setIdOauth(1);
		tblOAuth.setRefreshToken("");
		tblOAuth.setRefreshTokenExpiresIn("2682000");
		tblOAuth.setTokenKey("PARAM_KEY_OAUTH_TOKEN");
		tblOAuth.setTokenScope("scope1");
		tblOAuth.setTokenType("Bearer");

		ticketRequestMap.put("on_time_oauth", tblOAuth);

		TblOauth tblOAuthToRenew = new TblOauth();
		tblOAuthToRenew.setAccessToken("");
		tblOAuthToRenew.setConsentedOn("1599178967");
		tblOAuthToRenew.setExpiresIn("3600");
		tblOAuthToRenew.setIdOauth(1);
		tblOAuthToRenew.setRefreshToken("");
		tblOAuthToRenew.setRefreshTokenExpiresIn("2682000");
		tblOAuthToRenew.setTokenKey("PARAM_KEY_OAUTH_TOKEN");
		tblOAuthToRenew.setTokenScope("scope1");
		tblOAuthToRenew.setTokenType("Bearer");
		ticketRequestMap.put("to_renew_oauth", tblOAuthToRenew);
	}

	@Test
	void getOAuthValues_onTime() throws HttpClientErrorException, Exception {
		when(jpaOAuthRepository.findById(1)).thenReturn(Optional.of(ticketRequestMap.get("on_time_oauth")));

		OAuth oAuth = oAuthRepository.getOAuthValues();

		assertNotNull(oAuth);
	}

	@Test
	void getOAuthValues_toRenew() throws HttpClientErrorException, Exception {
		when(jpaOAuthRepository.findById(1)).thenReturn(Optional.of(ticketRequestMap.get("to_renew_oauth")));

		when(oAuthApi.generate(any(OAuth.class))).thenAnswer(new Answer<OAuth>() {
			@Override
			public OAuth answer(InvocationOnMock invocation) throws Throwable {
				return (OAuth) invocation.getArguments()[0];
			}
		});

		when(jpaOAuthRepository.save(any(TblOauth.class))).thenAnswer(new Answer<TblOauth>() {
			@Override
			public TblOauth answer(InvocationOnMock invocation) throws Throwable {
				return (TblOauth) invocation.getArguments()[0];
			}
		});

		OAuth oAuth = oAuthRepository.getOAuthValues();

		assertNotNull(oAuth);
	}

	@Test
	void getOAuthValues_nullOAuth() throws HttpClientErrorException, Exception {
		when(jpaOAuthRepository.findById(1)).thenReturn(Optional.empty());

		GenericDomainException exception = assertThrows(GenericDomainException.class, () -> {
			oAuthRepository.getOAuthValues();
		});

		assertEquals(exception.getMessage(), "OAuth couldn't be null");
	}
}
