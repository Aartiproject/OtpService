package com.vnc.otp.service;

import com.vnc.otp.dto.OtpRequest;
import com.vnc.otp.dto.OtpResponse;
import com.vnc.otp.dto.ResponseObject;

/**
 * This interface defines method to process otp generation request.
 * 
 * @author aarti
 *
 */
public interface RequestProcessor {

	/**
	 * This method process otp generation request received and sends back the
	 * desired response.
	 * 
	 * @param otpRequest {@link OtpResponse}} - request object to generate one time
	 *                   password corresponding to mobileNumber present in the
	 *                   request.
	 * 
	 * @return response {@link OtpResponse}
	 */
	ResponseObject<OtpResponse> processOtpGenerationRequest(final OtpRequest otpRequest);

}
