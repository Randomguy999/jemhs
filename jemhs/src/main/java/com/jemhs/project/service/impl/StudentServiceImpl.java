package com.jemhs.project.service.impl;

import com.jemhs.project.dao.StudentCredentialsDao;
import com.jemhs.project.dao.StudentDao;
import com.jemhs.project.model.Student;
import com.jemhs.project.model.StudentCredentials;
import com.jemhs.project.service.EmailService;
import com.jemhs.project.service.StudentService;
import com.jemhs.project.util.Constants;
import com.jemhs.project.util.RandomPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class StudentServiceImpl
  implements StudentService
{
  private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
  
  @Autowired
  StudentDao studentDao;
  @Autowired
  EmailService emailService;
  @Autowired
  StudentCredentialsDao studentCredentialsDao;
  
  public StudentServiceImpl() {}
  
  public Student registerStudent(Student student)
  {
    Student registeredStudent = null;
    StudentCredentials stdCred = null;
    StudentCredentials stuCred = new StudentCredentials();
    String userName = student.getLastName().substring(0, 5).toLowerCase() + 
      student.getFirstName().substring(0, 2).toLowerCase();
    stuCred.setUserName(userName);
    stuCred.setPassword(RandomPassword.generatePassword());
    
    String toUser = null;
    String subject = Constants.REGISTRATION_SUBJECT;
    String body = null;
    try {
      logger.info("Registering Student..." + student.getFirstName());
      registeredStudent = (Student)studentDao.save(student);
      logger.info("Successfully registered student " + student.getFirstName());
      stuCred.setId(registeredStudent.getId());
      stuCred.setEmail(registeredStudent.getEmail());
      stdCred = (StudentCredentials)studentCredentialsDao.save(stuCred);
      try {
        toUser = registeredStudent.getEmail();
        body = "<html><body>Hello " + registeredStudent.getFirstName() + ",<br><br>Your Registration is Success. <br><br>" + 
          "Username :" + stdCred.getUserName() + "<br><br>" + 
          "Password :" + stdCred.getPassword() + "<br><br>" + 
          "<a href='http://jemhs.com/studentlogin'>Click here </a>   to login</body></html><br><br>" + 
          "Regards," + "<br>" + "Admin";
        emailService.sendSimpleMessage(toUser, subject, body);
      } catch (Exception e) {
        logger.error("Error while Sending registration email...");
        e.printStackTrace();
      }
      
    }
    catch (Exception e)
    {
      logger.error("Error while registering student ...");
      e.printStackTrace();
    }
	return registeredStudent;
  }
  


  public boolean isValidUser(StudentCredentials student)
  {
    boolean isValidUser = false;
    try {
      logger.info("Check if the user is a valid student");
      StudentCredentials adm = studentCredentialsDao.findByuserName(student.getUserName());
      if (adm == null) {
        isValidUser = false;
      }
      else if (adm.getPassword().equals(student.getPassword())) {
        isValidUser = true;
      }
    }
    catch (Exception e) {
      logger.error("Error while checking if valid student user...");
      e.printStackTrace();
    }
    return isValidUser;
  }
  

  public boolean changePassword(StudentCredentials student)
  {
    boolean updatedPass = false;
    int updated = 0;
    try {
      StudentCredentials adm = studentCredentialsDao.findByuserName(student.getUserName());
      if (adm == null) {
        return updatedPass;
      }
      if (adm.getPassword().equals(student.getPassword())) {
        logger.info("Changing Password for " + student.getUserName());
        updated = studentCredentialsDao.changepassword(student.getNewPassword());
        if (updated != 0) {
          return true;
        }
      }
      else {
        updatedPass = false;
      }
    } catch (Exception e) {
      logger.error("Error while changing password...");
      e.printStackTrace();
    }
    return updatedPass;
  }
}