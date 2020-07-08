package com.company.resourceapi.exceptions;

import com.company.resourceapi.entities.Project;

public class NotFoundException extends RuntimeException{

  private static final long serialVersionUID = 6039979771371807626L;

  public NotFoundException(Class<Project> class1, long id) {
    super("Project with Id " + id + " not found");
	}
}
