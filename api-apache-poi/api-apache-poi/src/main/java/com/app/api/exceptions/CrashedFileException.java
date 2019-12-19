package com.app.api.exceptions;

public class CrashedFileException extends RuntimeException {

	public CrashedFileException() {
		super("ERROR: Crashed file");
	}

}
