package com.vnc.otp.repository.read;


 
import java.util.Optional;

import com.vnc.otp.entity.OtpAttemptsEntity;

public interface OtpAttemptsReadRepository {
	
	// write query

	Optional<OtpAttemptsEntity> findByMobileNumberAndScenario(final Long mobileNumber, final Integer scenario);

}
