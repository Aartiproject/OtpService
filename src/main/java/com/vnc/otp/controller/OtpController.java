package com.vnc.otp.controller;

import com.vnc.otp.config.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vnc.otp.dto.OtpRequest;
import com.vnc.otp.dto.OtpResponse;
import com.vnc.otp.dto.ResponseObject;
import com.vnc.otp.service.OtpValidationService;
import com.vnc.otp.service.RequestProcessor;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aarti
 */
@RestController
@CrossOrigin
public class OtpController {

	private static final Logger LOG = LoggerFactory.getLogger(OtpController.class);
	@Autowired
	private RequestProcessor otpRequestProcessor;
	
	@Autowired
	private OtpValidationService otpValidationService; 
    /**
	 * 
	 * @param otpRequest
	 * @return
	 */
	@PostMapping(path = "/v1/get_otp", produces = "application/json")
	public ResponseEntity<ResponseObject<OtpResponse>> getOtp(@Valid @RequestBody final OtpRequest otpRequest) {

		LOG.trace("-->ENTRY>> getOtp() :: {}", otpRequest);

		ResponseObject<OtpResponse> response = otpRequestProcessor.processOtpGenerationRequest(otpRequest);

		LOG.trace("<< EXIT <<-- getOtp() :: {}", response);

		return ResponseEntity.ok(response);

	}
	/**
	 * @param /otpValidation
	 * @return
	 */
	
	@PostMapping(path="/v1/validate_otp", produces="application/json")
	public ResponseEntity<ResponseObject<OtpResponse>> validateOtp(@Valid @RequestBody final OtpRequest otpRequest){
		LOG.trace("-->ENTRY>> validateOtp() :: {}", otpRequest);
		
		ResponseObject<OtpResponse>  response = otpValidationService.validateOtp(otpRequest); 
		
		LOG.trace("<< EXIT <<-- validateOtp() :: {}", response);
		return ResponseEntity.ok(response);
		
	}






}