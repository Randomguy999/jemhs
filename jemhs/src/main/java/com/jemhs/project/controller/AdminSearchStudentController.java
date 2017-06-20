/*package com.jemhs.project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jemhs.project.model.UserRegistration;
import com.jemhs.project.service.UserLoginService;

@RestController
public class AdminSearchStudentController {

	private static final Logger logger = LoggerFactory.getLogger(AdminSearchStudentController.class);

	@Autowired
	UserLoginService adminService;

	@RequestMapping("/search/{keyword}")
	public List<UserRegistration> byKeyword(@PathVariable(value = "keyword") String keyword) {
		logger.info("Finding student..."+keyword);
		return adminService.findStudent(keyword);
	}
	
	@RequestMapping("/searchbyid/{keyword}")
	public UserRegistration byKeywordId(@PathVariable(value = "keyword") int keyword) {
		logger.info("Finding student..."+keyword);
		return adminService.findStudentbyId(keyword);
	}
}
*/