package com.jemhs.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jemhs.project.model.User;
import com.jemhs.project.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public ModelAndView user() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		model.addObject("userName", user.getUserName());
		model.addObject("message", "Content Available Only for Users with Student Role");
		model.setViewName("home");
		return model;
	}

	@RequestMapping(value = "/faculty", method = RequestMethod.GET)
	public ModelAndView faculty() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		modelAndView.addObject("userName",  user.getUserName());
		modelAndView.addObject("message", "Content Available Only for Users with Faculty Role");
		modelAndView.setViewName("home");
		return modelAndView;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		model.addObject("userName", user.getUserName());
		model.addObject("message", "Content Available Only for Users with Admin Role");
		model.setViewName("home");
		return model;
	}
	
	@RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("accessdenied");
		return model;
	}
}
