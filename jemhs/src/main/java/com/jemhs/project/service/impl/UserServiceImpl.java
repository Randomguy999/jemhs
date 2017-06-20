package com.jemhs.project.service.impl;

import java.util.Arrays;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jemhs.project.model.Role;
import com.jemhs.project.model.User;
import com.jemhs.project.model.UserRegistration;
import com.jemhs.project.repository.RoleRepository;
import com.jemhs.project.repository.UserRegistrationRepository;
import com.jemhs.project.repository.UserRepository;
import com.jemhs.project.service.EmailService;
import com.jemhs.project.service.UserService;
import com.jemhs.project.util.Constants;
import com.jemhs.project.util.RandomPassword;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserRegistration registerUser(UserRegistration userRegister) {

		String toUser = null;
		String subject = Constants.REGISTRATION_SUBJECT;
		String body = null;
		String pass = null;

		User user = new User();
		try {
			userRegister.setActive(1);
			Role userRole = roleRepository.findByRole("ADMIN");
			userRegister.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userRegistrationRepository.save(userRegister);
		} catch (Exception e) {
			logger.error("Error in registering student");
		}
		try {
			user.setActive(1);
			user.setId(userRegister.getUserId());
			String userName = userRegister.getLastName().substring(0, 5).toLowerCase()
					+ userRegister.getFirstName().substring(0, 2).toLowerCase();
			user.setUserName(userName);
			pass = RandomPassword.generatePassword();
			logger.info("Generated Password is " + pass);
			user.setPassword(bCryptPasswordEncoder.encode(pass));
			userRepository.save(user);
		} catch (Exception e) {
			logger.error("Error in generating and saving user password");
		}

		logger.info("Successfully registered student " + userRegister.getFirstName());
		try {
			toUser = userRegister.getEmail();
			body = "<html><body>Hello " + userRegister.getFirstName()
					+ ",<br><br>Your Registration is Success. <br><br>" + "Username :" + user.getUserName() + "<br><br>"
					+ "Password :" + pass + "<br><br>"
					+ "<a href='http://jemhs.com:8080/login' style=text-decoration:none; >Click here </a>   to login</body></html><br><br>"
					+ "Regards," + "<br>" + "Admin";
			emailService.sendSimpleMessage(toUser, subject, body);
		} catch (Exception e) {
			logger.error("Error in Sending email to user after registration");
		}
		return userRegister;
	}

	@Override
	public User findUserByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public boolean changePassword(User user) {

		boolean updatedPass = false;
		int updated = 0;
		String newPass = null;
		try {
			User existingUser = userRepository.findByUserName(user.getUserName());
			if (existingUser == null) {
				return updatedPass;
			}
			if(bCryptPasswordEncoder.matches(user.getPassword(),existingUser.getPassword())) {
				logger.info("Changing Password for " + user.getUserName());
				newPass = bCryptPasswordEncoder.encode(user.getNewPassword());
				updated = userRepository.changepassword(newPass,user.getUserName());
				if (updated != 0) {
					return true;
				}
			} else {
				updatedPass = false;
			}
		} catch (Exception e) {
			logger.error("Error while changing password...");
			e.printStackTrace();
		}

		return updatedPass;
	}
}
