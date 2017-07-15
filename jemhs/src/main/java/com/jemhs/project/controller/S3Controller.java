package com.jemhs.project.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jemhs.project.model.Message;
import com.jemhs.project.service.S3Service;

@Controller
public class S3Controller {

	@Autowired
	S3Service s3Service;

	private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

	@RequestMapping(value = "/fileupload", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {

		logger.info("In File Upload Controller");
		model.setViewName("fileupload");
		return model;
	}

	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	@ResponseBody
	public Message uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName=null;
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    userName = authentication.getName();
		}
		return s3Service.uploadMultipleFileHandler(files,userName);
	}
}