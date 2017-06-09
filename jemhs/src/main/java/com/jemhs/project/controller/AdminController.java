package com.jemhs.project.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jemhs.project.model.Admin;
import com.jemhs.project.service.AdminService;

@Controller
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	AdminService adminService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@RequestMapping("/admin")
	public String showAdminLoginForm(Model map) {
		logger.info("In Admin Controller...::::::+++++");
		// Add admin object to the map
		map.addAttribute("admin", new Admin());
		return "admin";
	}

	@RequestMapping("/changePass")
	public String showChangePassForm(ModelMap map) {
		logger.info("In showChangePassForm Method...");
		// Add adminChangePass object to the map
		map.addAttribute("changePass", new Admin());
		return "changepassword";
	}

	@RequestMapping("/loginProcess")
	public String login(@Valid @ModelAttribute("admin") Admin admin, BindingResult theBindingResult, Model model) {
		boolean isValidUser = false;

		if (theBindingResult.hasErrors()) {
			logger.error("Errors in the submitted admin login form");
			return "admin";
		} else {
			isValidUser = adminService.isValidUser(admin);
			if (!isValidUser) {
				model.addAttribute("message", "Login Failed");
				return "admin";
			} else {
				model.addAttribute("message", "Login Success");
			}
		}
		return "admin-confirmation";
	}

	@RequestMapping("/changePassword")
	public String changeAdminPassword(@Valid @ModelAttribute("changePass") Admin admin, BindingResult theBindingResult,
			Model model) {

		boolean updatedPass = false;

		if (theBindingResult.hasErrors()) {
			logger.error("Errors in the submitted change Password form");
			return "changepassword";
		} else {
			updatedPass = adminService.changePassword(admin);
			if (!updatedPass) {
				model.addAttribute("message", "Enter correct Credentials");
				return "changepassword";
			} else {
				model.addAttribute("message", "Password changed successfully");
			}
		}
		return "success";
	}
}
