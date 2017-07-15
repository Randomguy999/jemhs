package com.jemhs.project.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jemhs.project.model.User;
import com.jemhs.project.model.UserDetails;
import com.jemhs.project.service.UserService;
import com.jemhs.project.util.Constants;

@Controller
public class CRFPasswordController {

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public String forgotPassword() {
		return "forgotPassword";
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String email) {
		UserDetails userDetails = userService.findUserByEmail(email);
		if (null == userDetails) {
			modelAndView.addObject("errorMessage", "Entered email doesn't exist in records");
			modelAndView.setViewName("forgotPassword");
			return modelAndView;
		}
		boolean generatedToken = userService.generatePassResetToken(userDetails);
		if (generatedToken) {
			modelAndView.addObject("successMessage", Constants.PASSWORD_EMAIL_LINK);
		}
		modelAndView.setViewName("forgotPassword");
		return modelAndView;
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(@RequestParam("token") String token,
			ModelAndView modelAndView) {
		String validate = null;
		validate = userService.validateToken(token);
		if (validate.equals("Invalid Token")) {
			modelAndView.addObject("warningMessage", validate + ". Please request a new link to reset password");
			modelAndView.setViewName("forgotPassword");
			return modelAndView;
		} else if (validate.equals("Token has expired")) {
			modelAndView.addObject("warningMessage", validate + ". Please request a new link to reset password");
			modelAndView.setViewName("forgotPassword");
			return modelAndView;
		}
		modelAndView.addObject("confirmationToken", token);
		modelAndView.setViewName("confirm");
		return modelAndView;
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ModelAndView processResetPasswordForm(ModelAndView modelAndView, BindingResult bindingResult,
			@RequestParam Map<String, String> requestParams, RedirectAttributes redir) {
		boolean resetFlag = false;
		resetFlag = userService.resetPassword(requestParams.get("token"),requestParams.get("password"));
		modelAndView.setViewName("confirm");
		if (resetFlag) {
			modelAndView.addObject("successMessage", "Your password has been set!");
			return modelAndView;
		} else {
			modelAndView.setViewName("forgotPassword");
			modelAndView.addObject("errorMessage", "Invalid Token. Request a new link to reset your password");
		}

		return modelAndView;
	}

	@RequestMapping(value = "/changepass", method = RequestMethod.GET)
	public String showChangePasswordForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "changepassword";
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public ModelAndView changePassword(@Valid User user,BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		boolean changedPass = false;
		changedPass = userService.changePassword(user);
		modelAndView.setViewName("changepassword");
		if (!changedPass) {
			modelAndView.addObject("errorMessage", "Enter correct Credentials");
		}else{
			modelAndView.addObject("successMessage", "Password changed successfully"); 			
		}

		return modelAndView;
	}
}
