package com.vnc.otp.dto;

import javax.validation.constraints.NotNull;

public class OtpRequest {

	//this request is send to service class
	
	private String otpDetailsId;

	@NotNull
	private Long mobileNumber;

	@NotNull
	private int scenario;

	private String deviceId;
	
    private Integer otpValue;
    
   

	public String getOtpDetailsId() {
		return otpDetailsId;
	}

	public void setOtpDetailsId(String otpDetailsId) {
		this.otpDetailsId = otpDetailsId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getScenario() {
		return scenario;
	}

	public void setScenario(int scenario) {
		this.scenario = scenario;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getOtpValue() {
		return otpValue;
	}

	public void setOtpValue(Integer otpValue) {
		this.otpValue = otpValue;
	}
	
	
 @Override
	public String toString() {
		return "OtpRequest [otpDetailsId=" + otpDetailsId + ", mobileNumber=" + mobileNumber + ", scenario=" + scenario
				+ ", deviceId=" + deviceId + ", otpValue=" + otpValue +  "]";
	}


	

	
}
