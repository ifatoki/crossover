package com.company.resourceapi.exceptions;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="Item not found")
public class NotFoundException extends RuntimeException{

	/**
   *
   */
  private static final long serialVersionUID = 6039979771371807626L;

  public NotFoundException(Class<Project> class1, long id) {

	}
  
  public NotFoundException(Class<SdlcSystem> class1) {

  }
}