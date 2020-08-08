package com.tdp.ms.autogestion.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCategory {

	// From Request
	INVALID_REQUEST("invalid-request", HttpStatus.BAD_REQUEST, "SVC0001",
			"Generic Client Error: invalid-request - additional info", "API Generic wildcard fault response"), // 400:
																												// Bad
																												// Request
	MISSING_MANDATORY("Missing mandatory parameter", HttpStatus.BAD_REQUEST, "SVC1000", "Missing mandatory parameter",
			"API Request without mandatory field"), // 400:
													// Bad
													// Request
	ARGUMENT_MISMATCH("argument-mismatch", HttpStatus.BAD_REQUEST, "SVC0001", "", ""), // 400: Bad Request
	UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED, "SVC0001", "", ""), // 401: Unauthorized
	FORBIDDEN("forbidden", HttpStatus.FORBIDDEN, "SVC0001", "", ""), // 403: Forbidden
	RESOURCE_NOT_FOUND("Not existing Resource Id", HttpStatus.NOT_FOUND, "SVC1006",
			"Resource %s does not exist. Resource Identifier",
			"Reference to a resource identifier which does not exist in the collection/repository referred (e.g.: invalid Id)"), // 404:
																																	// Not
																																	// found
	CONFLICT("conflict", HttpStatus.CONFLICT, "SVC0001", "", ""), // 409: Conflict
	PRECONDITION_FAILED("precondition-failed", HttpStatus.PRECONDITION_FAILED, "SVC0001", "", ""), // 412: Precondition
																									// failed
	INVALID_HEADER("invalid-header", HttpStatus.BAD_REQUEST, "SVC0001", "", ""), // 400: Bad Request

	// From Server
	EXTERNAL_ERROR("Generic Server Fault", HttpStatus.INTERNAL_SERVER_ERROR, "SVR1000", "Generic Server Error",
			"There was a problem in the Service Providers network that prevented to carry out the request"), // 500:
																												// Internal
																												// server
																												// error
	HOST_NOT_FOUND("host-not-found", HttpStatus.INTERNAL_SERVER_ERROR, "SVR1000", "", ""), // 500: Internal server error
	UNEXPECTED("unexpected", HttpStatus.INTERNAL_SERVER_ERROR, "SVR1000", "", ""), // 500: Internal server error
	NOT_IMPLEMENTED("not-implemented", HttpStatus.NOT_IMPLEMENTED, "SVR1000", "", ""), // 501: Not Implemented
	SERVICE_UNAVAILABLE("service-unavailable", HttpStatus.SERVICE_UNAVAILABLE, "SVR1000", "", ""), // 503: Service
																									// unavailable
	// (Circuit Breaker)
	EXTERNAL_TIMEOUT("external-timeout", HttpStatus.SERVICE_UNAVAILABLE, "SVR1000", "", ""); // 503: Service unavailable

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
