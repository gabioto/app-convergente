package com.tdp.ms.autogestion.exception;

public class ExternalServerException extends DomainException{

	private static final long serialVersionUID = -4421133771413546676L;

	public ExternalServerException(ErrorCategory error,
			String userMessage) {
		super(userMessage, error);
	}	
}
