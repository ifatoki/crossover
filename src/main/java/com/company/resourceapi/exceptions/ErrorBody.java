package com.company.resourceapi.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorBody {
  private final int code;
  private final String message;
  private final String error;
}
