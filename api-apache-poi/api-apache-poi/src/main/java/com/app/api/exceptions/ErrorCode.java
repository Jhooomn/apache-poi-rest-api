package com.app.api.exceptions;

public class ErrorCode {

	private String code;
	private String message;

	public ErrorCode() {
		// TODO Auto-generated constructor stub
	}
	
	

	public void setCode(String code) {
		this.code = code;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
