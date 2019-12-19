package com.app.api.exceptions;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

	public static final Logger LOG = Logger.getLogger(ExceptionsHandler.class.getName());

	private void logError(ErrorCode ec, Exception e) {
		LOG.severe(ec.getCode());
		LOG.severe(ec.getMessage());
		LOG.severe(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorCode generalException(Exception e) {

		ErrorCode ec = new ErrorCode();

		ec.setCode(this.getId());
		ec.setMessage("ERROR: File Crashed");

		logError(ec, e);

		return ec;
	}

	public String getId() {
		return UUID.randomUUID().toString();
	}

}
