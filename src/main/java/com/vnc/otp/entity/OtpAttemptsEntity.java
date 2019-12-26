package com.vnc.otp.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entity class to hold OTP Attempts related data
 * 
 * @author aarti
 *
 */
@Document(value = "otp_attempts")
public class OtpAttemptsEntity {

	@Id
	private String otpAttemptsId;

	@Field(value = "mobile_number")
	private Long mobileNumber;

	@Field(value = "scenario")
	private int scenario;

	@Field(value = "device_ids")
	private List<String> deviceIds;

	@Field(value = "number_of_otp_attempts")
	private int numberOfOtpAttempts;

	@Field(value = "created_at")
	private Long createdAt;

	@Field(value = "waiting_time")
	private Long waitingTime;

	@Field(value = "updated_at")
	private Long updatedAt;

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getOtpAttemptsId() {
		return otpAttemptsId;
	}

	public void setOtpAttemptsId(String otpAttemptsId) {
		this.otpAttemptsId = otpAttemptsId;
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

	public List<String> getDeviceIds() {

		if (Objects.isNull(this.deviceIds)) {
			deviceIds = new ArrayList<>();
		}
		return deviceIds;
	}

	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}

	public int getNumberOfOtpAttempts() {
		return numberOfOtpAttempts;
	}

	public void setNumberOfOtpAttempts(int numberOfOtpAttempts) {
		this.numberOfOtpAttempts = numberOfOtpAttempts;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Long waitingTime) {
		this.waitingTime = waitingTime;
	}

	@Override
	public String toString() {
		return "OtpAttemptsEntity [otpAttemptsId=" + otpAttemptsId + ", mobileNumber=" + mobileNumber + ", scenario="
				+ scenario + ", deviceIds=" + deviceIds + ", numberOfOtpAttempts=" + numberOfOtpAttempts
				+ ", createdAt=" + createdAt + ", waitingTime=" + waitingTime + ", updatedAt=" + updatedAt + "]";
	}

}
