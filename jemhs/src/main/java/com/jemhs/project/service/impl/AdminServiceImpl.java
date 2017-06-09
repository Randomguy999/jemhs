package com.jemhs.project.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jemhs.project.dao.AdminDao;
import com.jemhs.project.model.Admin;
import com.jemhs.project.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDao adminDao;

	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Override
	public boolean isValidUser(Admin admin) {

		boolean isValidUser = false;
		try {
			logger.info("Check if the user is a valid admin");
			Admin adm = adminDao.findByuserName(admin.getUserName());
			if (adm == null) {
				isValidUser = false;
			} else {
				if (adm.getPassword().equals(admin.getPassword())) {
					isValidUser = true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while checking if valid admin user...");
			e.printStackTrace();
		}
		return isValidUser;
	}

	@Override
	public boolean changePassword(Admin admin) {

		boolean updatedPass = false;
		int updated = 0;
		try {
			Admin adm = adminDao.findByuserName(admin.getUserName());
			if(adm==null){
				return updatedPass;
			}
			if (adm.getPassword().equals(admin.getPassword())) {
				logger.info("Changing Password for "+admin.getUserName());
				updated = adminDao.changepassword(admin.getNewPassword());
				if (updated != 0) {
					updatedPass = true;
					return updatedPass;
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
