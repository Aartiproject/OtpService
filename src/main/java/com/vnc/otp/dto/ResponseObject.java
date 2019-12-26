package com.vnc.otp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObject<T> {

	private ResponseStatus status;

	private T response;

	public ResponseObject() {
		super();
	}

	public ResponseObject(ResponseStatusCode statusCode) {
		this.status = new ResponseStatus(statusCode);
	}

	public ResponseObject(ResponseStatusCode statusCode, String actualErrorMessage) {
		this.status = new ResponseStatus(statusCode, actualErrorMessage);
	}

	public ResponseObject(ResponseStatusCode statusCode, Map<String, String> errors) {
		this.status = new ResponseStatus(statusCode, errors);
	}

	public ResponseObject(ResponseStatus status) {
		this.status = status;
	}

	public ResponseObject(T response, ResponseStatusCode statusCode) {
		this.response = response;
		this.status = new ResponseStatus(statusCode);
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public static <T> ResponseObject<T> success(T response) {
		return new ResponseObject<>(response, ResponseStatusCode.SUCCESS);
	}

	@Override
	public String toString() {
		return "ResponseObject [status=" + status + ", response=" + response + "]";
	}

}
