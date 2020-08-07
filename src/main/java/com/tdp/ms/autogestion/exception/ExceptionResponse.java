package com.tdp.ms.autogestion.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Value;

@Value
public class ExceptionResponse {

	private String exceptionId;

	private String userMessage;

	private String exceptionText;

	private String moreInfo;

	private HttpStatus status;

	private List<String> errors;

	public ExceptionResponse(String exceptionId, String userMessage, String exceptionText, String moreInfo,
			HttpStatus status, List<String> errors) {
		super();
		this.status = status;
		this.errors = errors;
		this.exceptionId = exceptionId;
		this.userMessage = userMessage;
		this.exceptionText = exceptionText;
		this.moreInfo = moreInfo;
	}

	public ExceptionResponse(String exceptionId, String userMessage, String exceptionText, String moreInfo,
			HttpStatus status, String error) {
		super();
		this.status = status;
		this.exceptionId = exceptionId;
		this.userMessage = userMessage;
		this.exceptionText = exceptionText;
		this.moreInfo = moreInfo;
		errors = Arrays.asList(error);
	}
}
