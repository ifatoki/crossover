package com.company.resourceapi.exceptions;

import com.company.resourceapi.entities.SdlcSystem;

public class NotFoundSdlcSystemException extends RuntimeException{

  private static final long serialVersionUID = -7070946915936323848L;

  public NotFoundSdlcSystemException(Class<SdlcSystem> class1, long id) {
    super("Sdlc System with Id " + id + " not found");
  }
}
