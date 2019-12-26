package com.vnc.otp.repository.read;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;


import com.vnc.otp.entity.OtpDetailsEntity;

@Repository
public interface OtpDetailsReadRepository {

	Optional<OtpDetailsEntity> findLatestOtpDetailsByScenarioAndMobileNumber( final Long mobileNUmber, final Integer scenario);

		//basis for mobilenumber and scenario basis
		
}




