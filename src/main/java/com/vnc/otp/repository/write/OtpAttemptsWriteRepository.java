package com.vnc.otp.repository.write;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vnc.otp.entity.OtpAttemptsEntity;

@Repository
public interface OtpAttemptsWriteRepository extends MongoRepository<OtpAttemptsEntity, String>  {

	
	
}
