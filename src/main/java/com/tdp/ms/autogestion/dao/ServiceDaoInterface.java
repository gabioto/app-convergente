package com.tdp.ms.autogestion.dao;

import com.tdp.ms.autogestion.model.OAuth;

public interface ServiceDaoInterface {
	
	public OAuth getOauth(int id) throws Exception;
}
