package com.tdp.ms.autogestion.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DomainExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { DomainException.class })
	ResponseEntity<ExceptionResponse> handleDomainException(DomainException ex) {

		ErrorCategory category = ex.getError();

		return new ResponseEntity<>(new ExceptionResponse(category.getExceptionId(), category.getUserMessage(),
				category.getExceptionText(), category.getMoreInfo(), category.getHttpStatus(), ex.getMessage()),
				category.getHttpStatus());
	}

	@ExceptionHandler(value = { ForbiddenException.class })
	ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException ex) {

		ErrorCategory category = ex.getError();

		return new ResponseEntity<>(new ExceptionResponse(category.getExceptionId(), category.getUserMessage(),
				category.getExceptionText(), category.getMoreInfo(), category.getHttpStatus(), ex.getMessage()),
				category.getHttpStatus());
	}

	@ExceptionHandler(value = { ValidRequestException.class })
	ResponseEntity<ExceptionResponse> handleValidRequestException(ValidRequestException ex) {

		ErrorCategory category = ex.getError();

		return new ResponseEntity<>(new ExceptionResponse(category.getExceptionId(), category.getUserMessage(),
				category.getExceptionText(), category.getMoreInfo(), category.getHttpStatus(), ex.getMessage()),
				category.getHttpStatus());
	}

	@ExceptionHandler({ GenericDomainException.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

		ErrorCategory category = ErrorCategory.UNEXPECTED;
		ExceptionResponse response = new ExceptionResponse(category.getExceptionId(), category.getUserMessage(),
				category.getExceptionText(), ex.getLocalizedMessage(), category.getHttpStatus(),
				category.getMoreInfo());
		return handleExceptionInternal(ex, response, new HttpHeaders(), response.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ErrorCategory category = ErrorCategory.MISSING_MANDATORY;
		ExceptionResponse response = new ExceptionResponse(category.getExceptionId(), category.getUserMessage(),
				category.getExceptionText(), ex.getLocalizedMessage(), category.getHttpStatus(), errors);
		return handleExceptionInternal(ex, response, headers, response.getStatus(), request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> constraintViolationException(HttpServletResponse resp) {
		ErrorCategory category = ErrorCategory.MISSING_MANDATORY;
		return new ResponseEntity<>(new ExceptionResponse(category.getExceptionId(), category.getUserMessage(),
				category.getExceptionText(), "", category.getHttpStatus(), ""), category.getHttpStatus());
	}

}