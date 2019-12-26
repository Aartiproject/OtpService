package com.vnc.otp.eum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum OtpScenario {
	
	SIGNUP(1,"SIGNUP"),
	LOGIN(2,"LOGIN"),
	SOCIAL_SIGNUP(3,"SOCIAL_SIGNUP" ),
	CHANGE_PASSWORD(4," CHANGE_PASSWORD" ),
	FORGET_PASSWORD(5,"FORGET_PASSWORD"),
	UPDATE_PHONE_NUMBER(6,"UPDATE_PHONE_NUMBER");

	private final int scenarioId;
	
	private final String scenarioName;
	private static final Map<Integer, OtpScenario> Otp_Attempts =
			Arrays.stream(OtpScenario.values())
					.collect(Collectors.toMap(OtpScenario::getScenarioId, Function.identity()));

	OtpScenario(int scenarioId, String scenarioName) {
		this.scenarioId = scenarioId;
		this.scenarioName = scenarioName;
	}

	public int getScenarioId() {
		return scenarioId;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	

}