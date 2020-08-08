package com.tdp.ms.autogestion.exception;

public class ResourceNotFoundException extends DomainException {

	public ResourceNotFoundException(String userMessage) {
		super(userMessage, ErrorCategory.RESOURCE_NOT_FOUND);
	}

	public ResourceNotFoundException(String messageFormat, Object... args) {
		super(String.format(messageFormat, args), ErrorCategory.RESOURCE_NOT_FOUND);
	}
}
