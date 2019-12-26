package com.vnc.otp.repository.read;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.vnc.otp.entity.OtpAttemptsEntity;
import com.vnc.otp.entity.OtpDetailsEntity;

@Service
public class OtpDetailsReadRepositoryImpl implements OtpDetailsReadRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Optional<OtpDetailsEntity> findLatestOtpDetailsByScenarioAndMobileNumber(final Long mobileNumber, final Integer scenario) {

		Query query = new Query(Criteria.where("mobile_number").is(mobileNumber));
		query.addCriteria(Criteria.where("scenario").is(scenario));
		query.with(Sort.by(Direction.DESC, "created_at"));
		return Optional.ofNullable(mongoTemplate.findOne(query, OtpDetailsEntity.class));

	}

	
}
