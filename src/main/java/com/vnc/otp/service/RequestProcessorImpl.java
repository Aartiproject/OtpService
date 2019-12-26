package com.vnc.otp.service;

import java.security.GeneralSecurityException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;
import com.vnc.otp.dto.OtpRequest;
import com.vnc.otp.dto.OtpResponse;
import com.vnc.otp.dto.ResponseObject;
import com.vnc.otp.dto.ResponseStatusCode;
import com.vnc.otp.entity.OtpAttemptsEntity;
import com.vnc.otp.entity.OtpDetailsEntity;
import com.vnc.otp.exception.FPlateBaseException;
import com.vnc.otp.repository.read.OtpAttemptsReadRepository;
import com.vnc.otp.repository.read.OtpDetailsReadRepository;
import com.vnc.otp.repository.write.OtpAttemptsWriteRepository;
import com.vnc.otp.repository.write.OtpDetailsWriteRepository;

/**
 * This is service implemetation for handling/processing otp generation request.
 *
 * @author aarti
 */
@Service
@RefreshScope
public class RequestProcessorImpl implements RequestProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(RequestProcessorImpl.class);

  @Value("${otp.maximum.otp.validation.attempts}")
  private Integer maximumOtpValidationAllowed;

  @Value("${otp.expiry.time}")
  private Long otpExpiryTime;

  // assign default values to variables and method arguments
  @Value("${otp.minimum.threshold.expiry:2500}")
  private Long minimumThresholdExpiryTime;

  @Value("${otp.waiting.time}")
  private Long waitingTime;

  @Value("${otp.waiting.time.post-unsuccessful-attempts}")
  private Long waitingTimeAfterThresholdUnsuccessfulAttempts;

  @Value("${otp.generation.threshold.attempts}")
  private Integer otpThresholdGenerationAttempts;

  @Autowired private OTPGenerator otpGenerator;

  @Autowired private OtpAttemptsWriteRepository otpAttemptsWriteRepository;

  @Autowired private OtpDetailsWriteRepository otpDetailsWriteRepository;

  @Autowired private OtpAttemptsReadRepository otpAttemptsReadRepository;

  @Autowired private OtpDetailsReadRepository otpDetailsReadRepository;

  /**
   * This method process otp generation request received and sends back the desired response.
   *
   * @param otpRequest {@link OtpResponse}} - request object to generate one time password
   *     corresponding to mobileNumber present in the request.
   * @return response {@link OtpResponse}
   */
  @Override
  public ResponseObject<OtpResponse> processOtpGenerationRequest(final OtpRequest otpRequest) {

    LOG.trace("-->>ENTRY>> processOtpGenerationRequest() :: {}", otpRequest);

    final Optional<OtpAttemptsEntity> optionalOtpAttemptEntity =
        otpAttemptsReadRepository.findByMobileNumberAndScenario(
            otpRequest.getMobileNumber(), otpRequest.getScenario());

    final Optional<OtpDetailsEntity> optionalOtpDetailsEntity =
        otpDetailsReadRepository.findLatestOtpDetailsByScenarioAndMobileNumber(
            otpRequest.getMobileNumber(), otpRequest.getScenario());

    final OtpResponse otpResponse = new OtpResponse();
    otpResponse.setMobileNumber(otpRequest.getMobileNumber());
    otpResponse.setScenario(otpRequest.getScenario());

    OtpAttemptsEntity otpAttemptsEntity = null;

    if (optionalOtpAttemptEntity.isPresent()
        && (optionalOtpDetailsEntity.isPresent()
            && optionalOtpDetailsEntity.get().getUsedAt() == 0)) {

      otpAttemptsEntity = optionalOtpAttemptEntity.get();

      if (!checkForNewDeviceId(otpAttemptsEntity, otpRequest.getDeviceId())
          && (otpAttemptsEntity.getWaitingTime() >= System.currentTimeMillis())) {
        LOG.debug(
            "-- processOtpGenerationRequest() --> Could not process the current request, as {} - waiting time is remaining for new otp generation request.",
            otpAttemptsEntity.getWaitingTime());

        otpResponse.setOtpResponseCode(ResponseStatusCode.OTP_WAITING_TIME_NOT_CROSSED.getCode());
        otpResponse.setWaitingTime(otpAttemptsEntity.getWaitingTime());

        return ResponseObject.success(otpResponse);
      }

      otpRequest.setOtpValue(checkForExistingOtp(optionalOtpDetailsEntity.get(), otpResponse));
      otpAttemptsEntity.setUpdatedAt(System.currentTimeMillis());

    } else {
      otpRequest.setOtpValue(generateNewOneTimePassword());
      otpResponse.setOtpReferenceId(generateReferenceId());

      OtpDetailsEntity otpDetailsEntity = new OtpDetailsEntity();
      otpDetailsEntity.setOtpValue(otpRequest.getOtpValue());
      otpDetailsEntity.setScenario(otpResponse.getScenario());
      otpDetailsEntity.setOtpDetailsId(otpResponse.getOtpReferenceId());
      otpDetailsEntity.setMobileNumber(otpResponse.getMobileNumber());
      otpDetailsEntity.setExpiryTime(System.currentTimeMillis() +otpExpiryTime);
      otpDetailsEntity.setCreatedAt(System.currentTimeMillis());
      persistOtpDetailsEntity(otpDetailsEntity);

      otpAttemptsEntity = new OtpAttemptsEntity();
      otpAttemptsEntity.setOtpAttemptsId(generateReferenceId());
      otpAttemptsEntity.setMobileNumber(otpRequest.getMobileNumber());
      otpAttemptsEntity.getDeviceIds().add(otpRequest.getDeviceId());
      otpAttemptsEntity.setScenario(otpRequest.getScenario());
      otpAttemptsEntity.setCreatedAt(System.currentTimeMillis());
    }

    otpRequest.setOtpDetailsId(otpResponse.getOtpReferenceId());
    checkAndSetOtpAttemptsEntity(otpAttemptsEntity);
    persistOtpAttemptsEntity(otpAttemptsEntity);

    otpResponse.setWaitingTime(otpAttemptsEntity.getWaitingTime());
    otpResponse.setOtpResponseCode(ResponseStatusCode.SUCCESS.getCode());
    LOG.trace("<<--EXIT<< processOtpGenerationRequest() :: {}", otpResponse);
    return ResponseObject.success(otpResponse);
  }

  /**
   * @param otpDetailsEntity {@link OtpDetailsEntity}
   * @param otpResponse {@link OtpResponse}
   * @return otpValue
   */
  private Integer checkForExistingOtp(
      final OtpDetailsEntity otpDetailsEntity, final OtpResponse otpResponse) {

    LOG.trace("-->>ENTRY>> checkForExistingOtp()");

    OtpDetailsEntity otpDetails = null;
    Integer otpValue = null;

    if (otpDetailsEntity.getNumberOfValidationAttempts() < maximumOtpValidationAllowed
        && (System.currentTimeMillis() + minimumThresholdExpiryTime)
            < otpDetailsEntity.getExpiryTime()) {

      otpDetails = otpDetailsEntity;
      otpResponse.setOtpReferenceId(otpDetails.getOtpDetailsId());
      otpValue = otpDetails.getOtpValue();

    } else {

      otpValue = generateNewOneTimePassword();
      otpResponse.setOtpReferenceId(generateReferenceId());

      otpDetails = new OtpDetailsEntity();
      otpDetails.setOtpValue(otpValue);
      otpDetails.setScenario(otpResponse.getScenario());
      otpDetails.setOtpDetailsId(otpResponse.getOtpReferenceId());
      otpDetails.setMobileNumber(otpResponse.getMobileNumber());
      otpDetails.setExpiryTime(System.currentTimeMillis() + otpExpiryTime);
      otpDetails.setCreatedAt(System.currentTimeMillis());
    }

    otpResponse.setOtpResponseCode(ResponseStatusCode.SUCCESS.getCode());

    persistOtpDetailsEntity(otpDetailsEntity);

    LOG.trace("<<--EXIT<< checkForExistingOtp()");

    return otpValue;
  }

  /**
   * This method persists otpDetailsEntity to Db
   *
   * @param otpDetailsEntity {@link OtpDetailsEntity}
   */
  private void persistOtpDetailsEntity(final OtpDetailsEntity otpDetailsEntity) {

    LOG.trace("-->>ENTRY>> persistOtpDetailsEntity() :: {}", otpDetailsEntity);
    OtpDetailsEntity detailsEntity = null;

    try {
      detailsEntity = otpDetailsWriteRepository.save(otpDetailsEntity);
    } catch (MongoException ex) {
      LOG.error(
          "--  persistOtpDetailsEntity() --> Encountered exception while saving otpDetailsEntity to DB. {}",
          ex);

      throw new FPlateBaseException(ResponseStatusCode.OTP_PERSISTENCE_ISSUE);
    }

    if (!otpDetailsEntity.equals(detailsEntity)) {
      throw new FPlateBaseException(ResponseStatusCode.OTP_PERSISTENCE_ISSUE);
    }

    LOG.trace("<<--EXIT<< persistOtpDetailsEntity()");
  }

  /**
   * This method persists otpAttemtsEntity to Db
   *
   * @param otpAttemptsEntity {@link OtpAttemptsEntity}
   */
  private void persistOtpAttemptsEntity(OtpAttemptsEntity otpAttemptsEntity) {
    LOG.trace("-->>ENTRY>>persistOtpAttempts() :: {}", otpAttemptsEntity);
    OtpAttemptsEntity attemptsEntity = null;
    try {
      attemptsEntity = otpAttemptsWriteRepository.save(otpAttemptsEntity);

    } catch (MongoException ex) {

      LOG.error(
          "--  persistOtpAttempts() --> Encountered exception while saving otpAttemptsEntity to DB. {}",
          ex);

      throw new FPlateBaseException(ResponseStatusCode.OTP_PERSISTENCE_ISSUE);
    }

    if (!otpAttemptsEntity.equals(attemptsEntity)) {
      throw new FPlateBaseException(ResponseStatusCode.OTP_PERSISTENCE_ISSUE);
    }

    LOG.trace("<<--EXIT<< persistOtpAttempts()");
  }

  /**
   * Method to generate one time password
   *
   * @return otp value - 6 digit otp value
   */
  private Integer generateNewOneTimePassword() {

    try {

      return otpGenerator.generateOneTimePassword();

    } catch (GeneralSecurityException e) {

      LOG.error("-- generateNewOneTimePassword() --> Error/Issues while generating new OTP: {}", e);

      throw new FPlateBaseException(ResponseStatusCode.OTP_GENERATION_FAILED);
    }
  }

  /**
   * This is a helper method to check if the request is from new device or not. if request is from
   * new device, the requested device id will be added to existing deviceIdList.
   *
   * @return response
   */
  private boolean checkForNewDeviceId(
      final OtpAttemptsEntity otpAttemptsEntity, final String newDeviceId) {

    boolean isNewDevice = false;

    if (!otpAttemptsEntity.getDeviceIds().contains(newDeviceId)) {
      otpAttemptsEntity.getDeviceIds().add(newDeviceId);
      isNewDevice = true;
    }
    return isNewDevice;
  }

  /**
   * Helper method to generate unique reference id
   *
   * @return referenceId - unique string value
   */
  private String generateReferenceId() {
    return "OTP-" + System.currentTimeMillis();
  }

  /**
   * This is helper method to set waitingTime and numberOfOtpAttempts in the entity.
   *
   * @param otpAttemptsEntity @{@link OtpAttemptsEntity}}
   */
  private void checkAndSetOtpAttemptsEntity(OtpAttemptsEntity otpAttemptsEntity) {

    if (otpAttemptsEntity.getNumberOfOtpAttempts() >= otpThresholdGenerationAttempts) {
      otpAttemptsEntity.setWaitingTime(System.currentTimeMillis() + waitingTimeAfterThresholdUnsuccessfulAttempts);
    } else {
      otpAttemptsEntity.setWaitingTime(System.currentTimeMillis() + waitingTime);
    }

    otpAttemptsEntity.setNumberOfOtpAttempts(otpAttemptsEntity.getNumberOfOtpAttempts() + 1);
  }
}
