package com.tdp.ms.autogestion.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.repository.datasource.api.OAuthApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaOAuthRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblOauth;

@Repository
public class OAuthRepositoryImpl implements OAuthRepository {

	@Autowired
	private OAuthApi oAuthApi;

	@Autowired
	private JpaOAuthRepository jpaOAuthRepository;

	@Override
	public OAuth getOAuthValues() {
		Optional<TblOauth> tblOauth = jpaOAuthRepository.findById(1);
		System.out.println("ingreso tblOauth:: " + tblOauth.get().getIdOauth());

		OAuth oAuth = tblOauth.get().fromThis();

		long timeDiff = new Date().getTime() - (Long.parseLong(oAuth.getConsentedOn()) * 1000);

		if (timeDiff >= ((Integer.parseInt(oAuth.getExpiresIn()) - 5) * 1000)) {
			oAuth = generateOAuth2Token(oAuth);
		}

		return oAuth;
	}

	private OAuth generateOAuth2Token(OAuth pOAuth) {
		OAuth lOAuthResponse = oAuthApi.generate(pOAuth);

		if (lOAuthResponse != null) {
			TblOauth tableOauth = jpaOAuthRepository.save(TblOauth.from(lOAuthResponse));
			System.out.println("update tableOauth:: " + tableOauth.getIdOauth());
		}

		return lOAuthResponse;
	}
}