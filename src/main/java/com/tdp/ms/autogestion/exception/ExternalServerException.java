package com.tdp.ms.autogestion.exception;

public class ExternalServerException extends DomainException{

	public ExternalServerException(ErrorCategory error,
			String userMessage) {
		super(userMessage, error);
	}	
}
