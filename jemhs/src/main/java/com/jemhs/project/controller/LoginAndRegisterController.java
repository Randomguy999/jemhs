package com.jemhs.project.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jemhs.project.model.User;
import com.jemhs.project.model.UserRegistration;
import com.jemhs.project.service.UserService;

@Controller
public class LoginAndRegisterController {

	private static final Logger logger = LoggerFactory.getLogger(LoginAndRegisterController.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("welcome");
		return modelAndView;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "admin/register", method = RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
		UserRegistration user = new UserRegistration();
		model.addAttribute("user", user);
		return "admin/registration";
	}

	@RequestMapping(value = "/admin/registration", method = RequestMethod.POST)
	public ModelAndView login(@Valid UserRegistration userRegister, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new UserRegistration());
		userRegister.setGender("Male");
		userRegister.setStandard("V");
		logger.info("User Details :::: " + userRegister);
		if (bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please Enter all Fields");
			modelAndView.setViewName("admin/registration");
		} else {
			userService.registerUser(userRegister);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new UserRegistration());
			modelAndView.setViewName("admin/registration");
		}

		return modelAndView;
	}

	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public ModelAndView user() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		model.addObject("userName", "Welcome " + user.getUserName());
		model.addObject("message", "Content Available Only for Users with Student Role");
		model.setViewName("home");
		return model;
	}

	@RequestMapping(value = "/faculty", method = RequestMethod.GET)
	public ModelAndView faculty() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getUserName());
		modelAndView.addObject("message", "Content Available Only for Users with Faculty Role");
		modelAndView.setViewName("home");
		return modelAndView;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		model.addObject("userName", "Welcome " + user.getUserName());
		model.addObject("message", "Content Available Only for Users with Admin Role");
		model.setViewName("home");
		return model;
	}

	@RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// User user = userService.findUserByUserName(auth.getName());
		// model.addObject("userName", "Welcome " + user.getUserName());
		// model.addObject("adminMessage","Content Available Only for Users with
		// Admin Role");
		model.setViewName("accessdenied");
		return model;
	}

	@RequestMapping(value = "/changepass", method = RequestMethod.GET)
	public String showChangePasswordForm(WebRequest request, Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "changepassword";
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public ModelAndView changePassword(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		boolean changedPass = false;
		changedPass = userService.changePassword(user);
		modelAndView.setViewName("changepassword");
		if (!changedPass) {
			modelAndView.addObject("message", "Enter correct Credentials");
		}
		modelAndView.addObject("message", "Password changed successfully");

		return modelAndView;
	}
}
