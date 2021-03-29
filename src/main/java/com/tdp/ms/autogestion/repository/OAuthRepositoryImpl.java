package com.tdp.ms.autogestion.repository;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.GenericDomainException;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.repository.datasource.api.OAuthApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaOAuthRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblOauth;

@Repository
public class OAuthRepositoryImpl implements OAuthRepository {

	private static final Log log = LogFactory.getLog(OAuthRepositoryImpl.class);
	
	@Autowired
	private OAuthApi oAuthApi;

	@Autowired
	private JpaOAuthRepository jpaOAuthRepository;

	@Override
	public OAuth getOAuthValues() throws Exception {
		OAuth oAuth = null;

		try {
			Optional<TblOauth> tblOauth = jpaOAuthRepository.findById(1);
			if (tblOauth.isPresent()) {
				oAuth = tblOauth.get().fromThis();

				long timeDiff = new Date().getTime() - (Long.parseLong(oAuth.getConsentedOn()) * 1000);

				if (timeDiff >= ((Integer.parseInt(oAuth.getExpiresIn()) - 5) * 1000)) {
					oAuth = generateOAuth2Token(oAuth);
				}

			} else {
				throw new GenericDomainException(ErrorCategory.UNEXPECTED, "OAuth couldn't be null");
			}
		} catch (Exception e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getMessage());
			
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}

		return oAuth;
	}

	private OAuth generateOAuth2Token(OAuth pOAuth) throws Exception {
		OAuth lOAuthResponse = oAuthApi.generate(pOAuth);

		if (lOAuthResponse != null) {
			TblOauth tableOauth = jpaOAuthRepository.save(TblOauth.from(lOAuthResponse));
		}

		return lOAuthResponse;
	}
}
