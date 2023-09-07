package org.jsp.api.exception;

public class InvalidCredentialsException extends RuntimeException{
	@Override
	public String getMessage() {
		return "Invalid Credential";
	}
}
