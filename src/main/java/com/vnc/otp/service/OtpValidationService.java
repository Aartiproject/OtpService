package com.vnc.otp.service;

import com.vnc.otp.dto.OtpRequest;
import com.vnc.otp.dto.OtpResponse;
import com.vnc.otp.dto.ResponseObject;

public interface OtpValidationService {
	
	ResponseObject<OtpResponse> validateOtp(final OtpRequest otpRequest); 

}
