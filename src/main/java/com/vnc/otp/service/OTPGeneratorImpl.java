package com.vnc.otp.service;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@RefreshScope
@Service
public class OTPGeneratorImpl implements OTPGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(OTPGeneratorImpl.class);

	@Value("${otp.config.otpAlgorithm:HmacSHA1}")
	private String otpAlgorithmName;

	@Value("${otp.config.otpKeyLength:6}")
	private int otpKeyLength;

	private static final int BASE_MODE_VALUE = 10;
	private static final int HEX_FIFTEEN = 0x0f;

	private SecretKey key;

	/**
	 * This method is called Post Construct call in order to generate one time
	 * secret key, which will be used while generating mac hash during otp
	 * generation process.
	 */
	@PostConstruct
	private void generateKey() {
		LOG.trace("-->>ENTRY>> generateKey()");
		try {
			final KeyGenerator keyGenerator = KeyGenerator.getInstance(otpAlgorithmName);
			keyGenerator.init(512);
			key = keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// not going to happen
			LOG.error("-- generateKey() >> No Matching algorithm found with {}, name. {}", otpAlgorithmName,
					e.getMessage());
		}
		LOG.trace("<<EXIT<<-- generateKey()");
	}

	/**
	 * This method generates otp value.
	 *
	 * @return otp value
	 */
	@Override
	public int generateOneTimePassword() throws GeneralSecurityException {

		LOG.trace("-->>ENTRY>> generateOneTimePassword()");
		byte[] text = new byte[8];
		long movingFactor = new Date().getTime();
		final AtomicLong mf = new AtomicLong(movingFactor);
		IntStream.range(0, text.length).map(i -> (text.length - i - 1)).forEach(i -> {
			text[i] = (byte) (mf.get() & 0xff);
			mf.set(mf.get() >> 8);
		});

		Mac macObj;
		LOG.debug("-- generateOneTimePassword() >> generating message authentication code with {} algorithm",
				otpAlgorithmName);

		macObj = Mac.getInstance(otpAlgorithmName);
		macObj.init(key);

		final byte[] hmac = macObj.doFinal(text);

		final int offset = hmac[hmac.length - 1] & HEX_FIFTEEN;
		final int otpModDivisor = (int) Math.pow(BASE_MODE_VALUE, otpKeyLength);
		int hmacTimedOTP = ((((hmac[offset] & 0x7f) << 24) | ((hmac[offset + 1] & 0xff) << 16)
				| ((hmac[offset + 2] & 0xff) << 8) | (hmac[offset + 3] & 0xff)) % otpModDivisor);

		if (hmacTimedOTP == 0) {

			hmacTimedOTP = generateOneTimePassword();
		}
		if (hmacTimedOTP < otpModDivisor) {
			hmacTimedOTP *= (int) Math.pow(BASE_MODE_VALUE, (otpKeyLength - String.valueOf(hmacTimedOTP).length()));
		}
		LOG.trace("<<EXIT<<-- generateOneTimePassword()");
		return hmacTimedOTP;
	}

}
