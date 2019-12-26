package com.vnc.otp.dto;

public class OtpResponse {

	private String otpReferenceId;

	private Long mobileNumber;

	private Integer scenario;

	private Integer otpResponseCode;

	private Long waitingTime;

	public String getOtpReferenceId() {
		return otpReferenceId;
	}

	public void setOtpReferenceId(String otpReferenceId) {
		this.otpReferenceId = otpReferenceId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getScenario() {
		return scenario;
	}

	public void setScenario(Integer scenario) {
		this.scenario = scenario;
	}

	public Integer getOtpResponseCode() {
		return otpResponseCode;
	}

	public void setOtpResponseCode(Integer otpResponseCode) {
		this.otpResponseCode = otpResponseCode;
	}
   
	public Long getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Long waitingTime) {
		this.waitingTime = waitingTime;
	}

	@Override
	public String toString() {
		return "OtpResponse [otpReferenceId=" + otpReferenceId + ", mobileNumber=" + mobileNumber + ", scenario="
				+ scenario + ", otpResponseCode=" + otpResponseCode + ", waitingTime=" + waitingTime + "]";
	}

}
