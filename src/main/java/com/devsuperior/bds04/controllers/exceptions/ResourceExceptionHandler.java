package com.devsuperior.bds04.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		err.setError("Validation exception");
		err.setMessage(exception.getMessage());
		err.setPath(request.getRequestURI());

		for (FieldError error : exception.getBindingResult().getFieldErrors()) {
			err.addError(error.getField(), error.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}

}
