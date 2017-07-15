package com.jemhs.project.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jemhs.project.model.UserRegistration;
import com.jemhs.project.service.UserService;

@Controller
public class RegisterController {

	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("welcome");
		return modelAndView;
	}
	
	@RequestMapping(value = "/file", method = RequestMethod.GET)
	public ModelAndView fileupload() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("fileupload");
		return modelAndView;
	}

	@RequestMapping(value = "admin/register", method = RequestMethod.GET)
	public String showRegistrationForm(ModelAndView model) {
		UserRegistration user = new UserRegistration();
		model.addObject("user", user);
		return "admin/registration";
	}

	@RequestMapping(value = "/admin/registration", method = RequestMethod.POST)
	public ModelAndView login(@Valid UserRegistration userRegister, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new UserRegistration());
		logger.info("User Details :::: " + userRegister);
		if (bindingResult.hasErrors()) {
			modelAndView.addObject("errorMessage", "Please Enter all Fields");
			modelAndView.setViewName("admin/registration");
		} else {
			userService.registerUser(userRegister);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new UserRegistration());
			modelAndView.setViewName("admin/registration");
		}

		return modelAndView;
	}
}
