package com.jemhs.project.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jemhs.project.service.EmailService;

@Component
public class DailyEmails {

	private static final Logger logger = LoggerFactory.getLogger(DailyEmails.class);
	
	@Autowired
	EmailService emailService;
	
//	@Scheduled(cron="0 6 * * * *")
	public void sendEmails(){
		logger.info("Sending email...");
	//	emailService.sendSimpleMessage();
	}
}
