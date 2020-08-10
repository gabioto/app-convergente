package com.tdp.ms.autogestion.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class DomainException extends RuntimeException {

	private static final long serialVersionUID = -1461096138865827919L;

	ErrorCategory error;

	public DomainException(String userMessage, ErrorCategory error) {
		super(userMessage);
		this.error = error;
	}
}
