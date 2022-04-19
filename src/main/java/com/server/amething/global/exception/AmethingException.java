package com.server.amething.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmethingException extends RuntimeException{

    private final ErrorCode errorCode;
}
