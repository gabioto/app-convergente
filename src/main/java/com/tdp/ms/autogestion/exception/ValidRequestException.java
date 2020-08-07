package com.tdp.ms.autogestion.exception;

public class ValidRequestException extends DomainException{

	public ValidRequestException(ErrorCategory error,
			String userMessage) {
		super(userMessage, error);
	}	
}
//4627
