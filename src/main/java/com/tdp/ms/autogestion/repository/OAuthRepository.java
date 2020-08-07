package com.tdp.ms.autogestion.repository;

import com.tdp.ms.autogestion.model.OAuth;

public interface OAuthRepository {

	OAuth getOAuthValues() throws Exception;
}
