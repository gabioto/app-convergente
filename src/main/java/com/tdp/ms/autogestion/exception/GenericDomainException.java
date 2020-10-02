package com.tdp.ms.autogestion.exception;

public class GenericDomainException extends DomainException{

	private static final long serialVersionUID = 5876962427698288005L;

	public GenericDomainException(ErrorCategory error,
			String userMessage) {
		super(userMessage, error);
	}	
}
