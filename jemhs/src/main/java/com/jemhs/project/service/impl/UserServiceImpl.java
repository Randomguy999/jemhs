package com.jemhs.project.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jemhs.project.model.PasswordResetToken;
import com.jemhs.project.model.Role;
import com.jemhs.project.model.User;
import com.jemhs.project.model.UserDetails;
import com.jemhs.project.model.UserRegistration;
import com.jemhs.project.repository.PasswordResetTokenRepository;
import com.jemhs.project.repository.RoleRepository;
import com.jemhs.project.repository.UserDetailsRepository;
import com.jemhs.project.repository.UserRegistrationRepository;
import com.jemhs.project.repository.UserRepository;
import com.jemhs.project.service.EmailService;
import com.jemhs.project.service.UserService;
import com.jemhs.project.util.Constants;
import com.jemhs.project.util.RandomPassword;

@Service
public class UserServiceImpl implements UserService {

	static final long ONE_MINUTE_IN_MILLIS = 60000;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	private PasswordResetTokenRepository passResetRepositoty;

	@Autowired
	private UserRegistrationRepository userRegistrationRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Calendar cal = Calendar.getInstance();
	String usr = null;
	String toUser = null;
	String body = null;
	String pass = null;
	String subject = null;;

	@Override
	public User findUserByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public UserRegistration registerUser(UserRegistration userRegister) {

		subject = Constants.REGISTRATION_SUBJECT;
		User user = new User();
		try {
			Role userRole = roleRepository.findByRole("ADMIN");
			userRegister.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userRegistrationRepository.save(userRegister);
		} catch (Exception e) {
			logger.error("Error in registering student");
		}
		try {
			user.setActive(1);
			user.setEmail(userRegister.getEmail());
			user.setId(userRegister.getUserId());
			String userName = userRegister.getLastName().substring(0, 5).toLowerCase()
					+ userRegister.getFirstName().substring(0, 2).toLowerCase();
			user.setUserName(userName);
			pass = RandomPassword.generatePassword();
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
					+ "<a href='http://jemhs.com/login' style=text-decoration:none; >Click here </a>   to login</body></html><br><br>"
					+ "Regards," + "<br>" + "Admin";
			emailService.sendSimpleMessage(toUser, subject, body);
		} catch (Exception e) {
			logger.error("Error in Sending email to user after registration");
		}
		return userRegister;
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
			if (bCryptPasswordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
				logger.info("Changing Password for " + user.getUserName());
				newPass = bCryptPasswordEncoder.encode(user.getNewPassword());
				updated = userRepository.changepassword(newPass, user.getUserName());
				if (updated != 0) {
					logger.info("Successfully changed password for " + user.getUserName());
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

	@Override
	public boolean generatePassResetToken(UserDetails userDetails) {
		boolean generatedToken = false;
		PasswordResetToken resetToken = new PasswordResetToken();
		PasswordResetToken passResetToken =passResetRepositoty.findByUserName(userDetails.getUserName());
		if(null!=passResetToken){
		passResetRepositoty.deleteByUserName(userDetails.getUserName());	
		}
		long t = cal.getTimeInMillis();
		Date tokenExpiry = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));
		String token = UUID.randomUUID().toString();
		resetToken.setToken(token);
		resetToken.setUser_name(userDetails.getUserName());
		resetToken.setExpiryDate(tokenExpiry);
		toUser = userDetails.getEmail();
		subject = Constants.PASSWORD_RESET_SUBJECT;
		// body = "http://jemhs.com:8080" + "/resetPassword?id=" +
		// userDetails.getUserName() + "&token=" + token;
		body = "<html><body>Hello " + userDetails.getFirstName()
				+ ",<br><br>We've received a request to reset your account password.<br>"
				+ " If you did not make this request, you can ignore this email and your password will remain unchanged.<br> <br>"
				+ "To reset your password please follow the link below.\n(Link expires in 10 minutes)<br><br>"
				+ "<a href='http://jemhs.com/resetPassword?id=" + userDetails.getUserName() + "&token=" + token
				+ "'" + " style=text-decoration:none; >Reset Password </a></body></html><br><br>" + "Regards," + "<br>"
				+ "Admin";
		try {
			passResetRepositoty.save(resetToken);
			emailService.sendSimpleMessage(toUser, subject, body);
			generatedToken = true;
		} catch (Exception e) {
			logger.error("Error in generating password reset token");
		}
		return generatedToken;
	}

	@Override
	public String validateToken(String userName, String resetToken) {
		usr = userName;
		PasswordResetToken passToken = null;
		try {
			passToken = passResetRepositoty.findByUserName(userName);
		} catch (Exception e) {
			logger.error("Error in finding username");
		}
		if (null == resetToken || null == passToken || !passToken.getUser_name().equals(userName)
				|| !resetToken.equals(passToken.getToken())) {
			return "Invalid Token";
		}
		logger.info("Token time " + String.valueOf(passToken.getExpiryDate().getTime() - cal.getTime().getTime()));
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

			passResetRepositoty.delete(passToken.getId());
			return "Token has expired";
		}
		return "valid";
	}

	public boolean resetPassword(String pass) {
		boolean reset = false;
		String newPass = bCryptPasswordEncoder.encode(pass);
		try {
			PasswordResetToken passToken = passResetRepositoty.findByUserName(usr);
			userRepository.changepassword(newPass, usr);
			reset = true;
			passResetRepositoty.delete(passToken.getId());
		} catch (Exception e) {
			logger.error("Error in resetting password for " + usr);
		}
		return reset;
	}

	@Override
	public UserDetails findUserByEmail(String email) {
		UserDetails details = null;
		try {
			details = userDetailsRepository.findByEmail(email);
		} catch (Exception e) {
			logger.error("Error in finding the user for email " + email);
		}
		return details;
	}

}
