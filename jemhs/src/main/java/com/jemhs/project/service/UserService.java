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
	
	String validateToken(String userName,String resetToken);
	
	boolean resetPassword(String pass);
	
	boolean changePassword(User user);
	
	
}
