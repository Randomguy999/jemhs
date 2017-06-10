package com.jemhs.project.service.impl;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jemhs.project.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
  
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
    @Autowired
    JavaMailSender emailSender;
 
    public void sendSimpleMessage(String toUser, String subject, String body) {
       try{
    	   MimeMessage message = emailSender.createMimeMessage();
    	           MimeMessageHelper helper = new MimeMessageHelper(message, true);
    	           helper.setTo(toUser);
    	           helper.setText(body, true);
    	           helper.setSubject(subject);
    //	           ClassPathResource file = new ClassPathResource("cat.jpg");
    //	           helper.addInline("id101", file);
    	           logger.info("Sending email...");
    	           emailSender.send(message);
       }
       catch(Exception e){
    	   logger.error("Error while sending email...");
       }
    }
    
    
}