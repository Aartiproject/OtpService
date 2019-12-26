package com.vnc.otp.service;

import java.security.GeneralSecurityException;

public interface OTPGenerator {

	int generateOneTimePassword() throws GeneralSecurityException;

}
