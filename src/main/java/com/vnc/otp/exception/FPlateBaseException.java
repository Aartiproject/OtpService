package com.vnc.otp.exception;

import com.vnc.otp.dto.ResponseStatusCode;

public class FPlateBaseException extends RuntimeException  {

    private static final long serialVersionUID = 1L;
    private final Integer code;
    private final String message;

    public FPlateBaseException(ResponseStatusCode statusCode) {
        super();
        this.code = statusCode.getCode();
        this.message = statusCode.getReasonPhrase();
    }
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
