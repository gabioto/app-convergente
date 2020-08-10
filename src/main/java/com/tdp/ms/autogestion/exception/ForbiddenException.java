package com.tdp.ms.autogestion.exception;

public class ForbiddenException extends DomainException {

	private static final long serialVersionUID = -7575872681958618483L;

	public ForbiddenException(String userMessage) {
		super(userMessage, ErrorCategory.FORBIDDEN);
	}
}
