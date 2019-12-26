package com.vnc.otp.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;
import com.vnc.otp.dto.OtpRequest;
import com.vnc.otp.dto.OtpResponse;
import com.vnc.otp.dto.ResponseObject;
import com.vnc.otp.dto.ResponseStatusCode;
import com.vnc.otp.entity.OtpDetailsEntity;
import com.vnc.otp.exception.FPlateBaseException;
import com.vnc.otp.repository.read.OtpDetailsReadRepository;
import com.vnc.otp.repository.write.OtpDetailsWriteRepository;

/**
 * @author aarti
 */
@Service
public class OTPValidationServiceImpl implements OtpValidationService {

	private static final Logger LOG = LoggerFactory.getLogger(OTPValidationServiceImpl.class);

	@Value("${otp.maximum.validation.attempts:3}")
	private Integer maximumNumberOfValidationAllowed;

	@Autowired
	private OtpDetailsWriteRepository otpDetailsWriteRepository;

	@Autowired
	private OtpDetailsReadRepository otpDetailsReadRepository;

	/**
	 *
	 * @param otpRequest
	 * @return
	 */
	@Override
	public ResponseObject<OtpResponse> validateOtp(OtpRequest otpRequest) {

		LOG.trace("-->>ENTRY>> validateOtp() :: {}", otpRequest);

		final OtpResponse otpResponse = new OtpResponse();

		otpResponse.setMobileNumber(otpRequest.getMobileNumber());
		otpResponse.setScenario(otpRequest.getScenario());

		Integer statusCode = ResponseStatusCode.FAIL.getCode();

		final Optional<OtpDetailsEntity> optionalOtpDetailsEntity = otpDetailsReadRepository
				.findLatestOtpDetailsByScenarioAndMobileNumber(otpRequest.getMobileNumber(), otpRequest.getScenario());

		if (optionalOtpDetailsEntity.isPresent()) {

			final OtpDetailsEntity otpDetailsEntity = optionalOtpDetailsEntity.get();

			otpResponse.setOtpReferenceId(otpDetailsEntity.getOtpDetailsId());

			if (otpDetailsEntity.getNumberOfValidationAttempts() >= maximumNumberOfValidationAllowed) {

				LOG.debug(
						"-- validateOtp() --> Current otp request cannot be validated as maximum number of validation attempts ({}) are reached. numberOfValidationAttempts : {}",
						maximumNumberOfValidationAllowed, otpDetailsEntity.getNumberOfValidationAttempts());

				statusCode = ResponseStatusCode.OTP_VALIDATION_ATTEMPTS_REACHED.getCode();

			} else if ((otpDetailsEntity.getExpiryTime() >= System.currentTimeMillis())
					&& (otpDetailsEntity.getUsedAt() == 0)
					&& (otpDetailsEntity.getOtpDetailsId().equals(otpRequest.getOtpDetailsId()))
					&& otpDetailsEntity.getOtpValue() == otpRequest.getOtpValue()) {

				otpDetailsEntity.setUsedAt(System.currentTimeMillis());
				
				statusCode = ResponseStatusCode.SUCCESS.getCode();
			}
			
			otpDetailsEntity.setNumberOfValidationAttempts(otpDetailsEntity.getNumberOfValidationAttempts() + 1);
			
			persistOtpDetailsEntity(otpDetailsEntity);	

		} else {

			LOG.error("-- validateOtp() --> No matching otp records found from DB. Hence throwing exception");

			throw new FPlateBaseException(ResponseStatusCode.NO_MATCHING_OTP_RECORDS_FOUND);

		}
		otpResponse.setOtpResponseCode(statusCode);
		LOG.trace("<<--EXIT<< validateOtp() :: {}", otpResponse);
		return ResponseObject.success(otpResponse);
	}
	
	private void persistOtpDetailsEntity(final OtpDetailsEntity otpDetailsEntity) {

		LOG.trace("-->>ENTRY>> persistOtpDetailsEntity()>> :: {}", otpDetailsEntity);
		OtpDetailsEntity detailsEntity = null;

		try {
			detailsEntity = otpDetailsWriteRepository.save(otpDetailsEntity);
		} catch (MongoException ex) {
			LOG.error("--  persistOtpDetailsEntity() --> Encountered exception while saving otpDetailsEntity to DB. {}", ex);

			throw new FPlateBaseException(ResponseStatusCode.OTP_PERSISTENCE_ISSUE);
		}

		if (!otpDetailsEntity.equals(detailsEntity)) {
			throw new FPlateBaseException(ResponseStatusCode.OTP_PERSISTENCE_ISSUE);
		}

		LOG.trace("<<--EXIT<< persistOtpDetailsEntity()");
	}


}