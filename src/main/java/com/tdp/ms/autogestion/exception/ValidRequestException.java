package com.tdp.ms.autogestion.exception;

public class ValidRequestException extends DomainException{

	private static final long serialVersionUID = 5876962427698288005L;

	public ValidRequestException(ErrorCategory error,
			String userMessage) {
		super(userMessage, error);
	}	
}
