package com.jemhs.project.service;

import com.jemhs.project.model.User;
import com.jemhs.project.model.UserRegistration;

public interface UserService {

	UserRegistration registerUser(UserRegistration user);
	
	User findUserByUserName(String userName);
	
	boolean changePassword(User user);
	
	
}
