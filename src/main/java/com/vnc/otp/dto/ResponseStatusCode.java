package com.vnc.otp.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ResponseStatusCode {

	SUCCESS(10000, "SUCCESS"),
	FAIL(10001, "FAIL"),

	UNKNOWN_ERROR_OCCURRED(10002, "UNKNOWN_ERROR_OCCURRED"),

	INVALID_REQUEST_PARAMETER(10003, "INVALID_REQUEST_PARAMETER"),

	
	//otp related status codes
	OTP_WAITING_TIME_NOT_CROSSED(50000, "OTP_WAITING_TIME_NOT_CROSSED"),
	OTP_VALIDATION_ATTEMPTS_REACHED(50001, "OTP_VALIDATION_ATTEMPTS_REACHED"),

	// otp Error
	OTP_GENERATION_FAILED(50005, "OTP_GENERATION_FAILED"), 
	OTP_PERSISTENCE_ISSUE(50006, "OTP_PERSISTENCE_ISSUE"),
	NO_MATCHING_OTP_RECORDS_FOUND(50007, "NO_MATCHING_OTP_RECORDS_FOUND");

	private final Integer code;
	private final String reasonPhrase;

	private static final Map<Integer, ResponseStatusCode> RESPONSE_STATUS_CODE_MAP = Arrays
			.stream(ResponseStatusCode.values())
			.collect(Collectors.toMap(ResponseStatusCode::getCode, Function.identity()));

	private ResponseStatusCode(Integer code, String reasonPhrase) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
	}

	public Integer getCode() {
		return code;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

}
