package com.jemhs.project.service;

import com.jemhs.project.model.Admin;

public interface AdminService {

	boolean isValidUser(Admin admin);
	
	boolean changePassword(Admin admin);
	
}
