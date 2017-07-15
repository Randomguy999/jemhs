package com.jemhs.project.service;

import com.jemhs.project.model.User;
import com.jemhs.project.model.UserDetails;
import com.jemhs.project.model.UserRegistration;

public interface UserService {

	UserRegistration registerUser(UserRegistration user);
	
	UserDetails findUserByEmail(String email);
	
	User findUserByUserName(String userName);
	
//	User forgotPassword(String email);
	
	boolean generatePassResetToken(UserDetails userDetails);
	
	String validateToken(String resetToken);
	
	boolean resetPassword(String token,String password);
	
	boolean changePassword(User user);
	
	boolean contactUs(String name, String email, String reason, String message);
	
	
}
