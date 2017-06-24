package com.jemhs.project.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jemhs.project.repository.PasswordResetTokenRepository;

@Component
public class CustomSchedulers {

	private static final Logger logger = LoggerFactory.getLogger(CustomSchedulers.class);
	
	@Autowired
	PasswordResetTokenRepository passResetRepository;
	
	public void deleteExpiredTokens(){
		
		logger.info("Deleting the expired tokens..");
		
		
	}
}
