package com.company.resourceapi.exceptions;

public class InvalidRequestBodyException extends RuntimeException{

  private static final long serialVersionUID = 2798866563527494196L;

  public InvalidRequestBodyException(String bodyParam) {
    super(bodyParam + " in the request body is invalid");
	}
}
