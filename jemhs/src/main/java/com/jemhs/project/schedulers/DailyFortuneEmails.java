/*package com.jemhs.project.schedulers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jemhs.project.model.UserRegistration;
import com.jemhs.project.repository.StudentDao;
import com.jemhs.project.service.EmailService;
import com.jemhs.project.util.Constants;
import com.jemhs.project.util.DailyFortunes;

@Component
public class DailyFortuneEmails {

	private static final Logger logger = LoggerFactory.getLogger(DailyFortuneEmails.class);
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	StudentDao studentDao;
	
//	@Scheduled(cron="0 15 20 * * *")
	public void sendEmails(){
		
		final String subject = Constants.FORTUNE_SUBJECT;
        String body = null;
		
		logger.info("Sending Daily Fortune Email...");
		try{
			List<UserRegistration> studentList = studentDao.findAll();
			for(UserRegistration student: studentList){
				body="<html><body> "
						+ "Hi "+student.getFirstName()+",<br><br>"
						+"Good Morning ,<br><br>"
						+"Your Horoscope for today :<br><br>"
						+DailyFortunes.getDailyFortune()+"<br><br>"
						+"Have a Good Day<br><br><br>"+"Regards ,<br>Admin"
						+ " </body></html>";
				emailService.sendSimpleMessage(student.getEmail(), subject, body);
			}
			
		}
		catch(Exception e){
			
		}
	}
}
*/