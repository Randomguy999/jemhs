package com.jemhs.project.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jemhs.project.service.ReCaptchaService;
import com.jemhs.project.service.UserService;

@Controller
public class ReCaptchaController {

	@Autowired
	UserService userService;
	
	@Autowired
	ReCaptchaService reCaptchaService;
	
	@Autowired
	HttpServletRequest httpServletRequest;
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	 public ModelAndView contactUs() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("contact");
		return modelAndView;
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public ModelAndView contact(@RequestParam Map<String, String> requestParams,ModelAndView modelAndView ) {
		boolean isValidreCaptchaResponse = false;
		boolean mailSent= false;
		String remoteIp= getRemoteIp(httpServletRequest);
		String response = requestParams.get("g-recaptcha-response");
		if(null!=response){
			isValidreCaptchaResponse = reCaptchaService.isResponseValid(remoteIp,response );
		}
		if(isValidreCaptchaResponse){
		 mailSent =userService.contactUs(requestParams.get("name"), requestParams.get("email"),
					requestParams.get("reason"),requestParams.get("message"));	
		}
		if(mailSent){
			modelAndView.addObject("successMessage", "Thank you for contacting us."
					+ " We will respond to you with in 24 hours.."); 	
		}else{
			modelAndView.addObject("errorMessage", "An error has occured.Please try later..");			
		}
		modelAndView.setViewName("contact");
		return modelAndView;
	}
	
	private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
