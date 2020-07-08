package com.company.resourceapi.exceptions;

public class ConflictException extends RuntimeException{

  private static final long serialVersionUID = 6039979771371807626L;

  public ConflictException(long sdlcSystemId, String externalId) {
    super("Project with externalId: " + externalId + " and sdlcSystem with Id: " + sdlcSystemId + " already exists");
	}
}
