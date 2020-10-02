package com.tdp.ms.autogestion.exception;

import org.springframework.http.HttpStatus;

import com.tdp.ms.autogestion.util.Constants;

public enum ErrorCategory {

	// From Request
	INVALID_REQUEST("invalid-request", HttpStatus.BAD_REQUEST, Constants.SVC0001,
			"Generic Client Error: invalid-request - additional info", "API Generic wildcard fault response"),

	MISSING_MANDATORY("Missing mandatory parameter", HttpStatus.BAD_REQUEST, Constants.SVC1000,

			"Missing mandatory parameter", "API Request without mandatory field"), // 400: Bad Request

	ARGUMENT_MISMATCH("argument-mismatch", HttpStatus.BAD_REQUEST, Constants.SVC0001, "", ""), // 400: Bad Request

	UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED, Constants.SVC1002, "", ""), // 401: Unauthorized

	FORBIDDEN("forbidden", HttpStatus.FORBIDDEN, Constants.SVC1002, "Ticket creation limit",
			"User can´t create more tickets"),

	RESOURCE_NOT_FOUND("Not existing Resource Id", HttpStatus.NOT_FOUND, Constants.SVC1006,
			"Resource %s does not exist. Resource Identifier",
			"Reference to a resource identifier which does not exist in the collection/repository referred (e.g.: invalid Id)"),

	CONFLICT("conflict", HttpStatus.CONFLICT, Constants.SVC0001, "", ""), // 409: Conflict

	PRECONDITION_FAILED("precondition-failed", HttpStatus.PRECONDITION_FAILED, Constants.SVC0001, "", ""),

	INVALID_HEADER("invalid-header", HttpStatus.BAD_REQUEST, Constants.SVC0001, "", ""),

	// From Server
	EXTERNAL_ERROR("Generic Server Fault", HttpStatus.INTERNAL_SERVER_ERROR, Constants.SVR1000, "Generic Server Error",
			"There was a problem in the Service Providers network that prevented to carry out the request"),

	HOST_NOT_FOUND("host-not-found", HttpStatus.INTERNAL_SERVER_ERROR, Constants.SVR1000, "", ""),

	UNEXPECTED("unexpected", HttpStatus.INTERNAL_SERVER_ERROR, Constants.SVR1000, "Generic error exception",
			"An error occurred"), // 500: Internal server error

	NOT_IMPLEMENTED("not-implemented", HttpStatus.NOT_IMPLEMENTED, Constants.SVR1000, "", ""),

	SERVICE_UNAVAILABLE("service-unavailable", HttpStatus.SERVICE_UNAVAILABLE, Constants.SVR1000, "", ""),

	// (Circuit Breaker)
	EXTERNAL_TIMEOUT("external-timeout", HttpStatus.SERVICE_UNAVAILABLE, Constants.SVR1000, "", "");

	private static final String PROPERTY_PREFIX = "application.autogestion.averia.error-code.";

	private String userMessage;
	private String exceptionId;
	private String exceptionText;
	private String moreInfo;
	private HttpStatus httpStatus;

	ErrorCategory(String userMessage, HttpStatus httpStatus, String exceptionId, String exceptionText,
			String moreInfo) {
		this.userMessage = PROPERTY_PREFIX.concat(userMessage);
		this.httpStatus = httpStatus;
		this.exceptionId = exceptionId;
		this.exceptionText = exceptionText;
		this.moreInfo = moreInfo;
	}

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public String getExceptionId() {
		return exceptionId;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public String getExceptionText() {
		return exceptionText;
	}

	public String getMoreInfo() {
		return moreInfo;
	}
}
