package com.vnc.otp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entity class for holding otp details related information
 * 
 * @author aarti
 */

@Document(value = "otp_details")
public class OtpDetailsEntity {

	@Id
	@Field(value = "reference_id")
	private String otpDetailsId;

	@Field(value = "mobile_number")
	private Long mobileNumber;

	@Field(value = "otp_value")
	private int otpValue;

	@Field(value = "number_of_validation_attempts")
	private int numberOfValidationAttempts;

	@Field(value = "expiry_time")
	private Long expiryTime;

	@Field(value = "used_at")
	private long usedAt; 

	@Field(value = "scenario")
	private int scenario;

	@Field(value = "created_at")
	private Long createdAt;

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

	public int getOtpValue() {
		return otpValue;
	}

	public void setOtpValue(int otpValue) {
		this.otpValue = otpValue;
	}

	public Long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public long getUsedAt() {
		return usedAt;
	}

	public void setUsedAt(long usedAt) {
		this.usedAt = usedAt;
	}

	public int getScenario() {
		return scenario;
	}

	public void setScenario(int scenario) {
		this.scenario = scenario;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public int getNumberOfValidationAttempts() {
		return numberOfValidationAttempts;
	}

	public void setNumberOfValidationAttempts(int numberOfValidationAttempts) {
		this.numberOfValidationAttempts = numberOfValidationAttempts;
	}

	@Override
	public String toString() {
		return "OtpDetailsEntity [otpDetailsId=" + otpDetailsId + ", mobileNumber=" + mobileNumber + ", otpValue="
				+ otpValue + ", numberOfValidationAttempts=" + numberOfValidationAttempts + ", expiryTime=" + expiryTime
				+ ", usedAt=" + usedAt + ", scenario=" + scenario + ", createdAt=" + createdAt + "]";
	}

}
