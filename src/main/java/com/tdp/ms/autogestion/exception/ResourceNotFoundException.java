package com.tdp.ms.autogestion.exception;

public class ResourceNotFoundException extends DomainException {

	private static final long serialVersionUID = 8407099965866256313L;

	public ResourceNotFoundException(String userMessage) {
		super(userMessage, ErrorCategory.RESOURCE_NOT_FOUND);
	}

	public ResourceNotFoundException(String messageFormat, Object... args) {
		super(String.format(messageFormat, args), ErrorCategory.RESOURCE_NOT_FOUND);
	}
}
