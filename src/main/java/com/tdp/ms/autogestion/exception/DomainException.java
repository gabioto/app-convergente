package com.tdp.ms.autogestion.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class DomainException extends RuntimeException {
	ErrorCategory error;

	public DomainException(String userMessage, ErrorCategory error) {
		super(userMessage);
		this.error = error;
	}
}

