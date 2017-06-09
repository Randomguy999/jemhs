package com.jemhs.project.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jemhs.project.model.Student;
import com.jemhs.project.service.StudentService;

@Controller
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	StudentService studentService;

	@RequestMapping("/showRegistrationForm")
	public String showStudentRegistrationForm(Model map) {
		logger.info("In Show Student Registration method...");
		// Add student registration object to the map
		map.addAttribute("studentRegistration", new Student());
		return "registration-form";
	}

	@RequestMapping("/RegistrationProcess")
	public String login(@Valid @ModelAttribute("studentRegistration") Student student, BindingResult theBindingResult,
			Model model) {

		logger.info("Student Details :::: " + student);
		if (theBindingResult.hasErrors()) {
			logger.error("Errors in the submitted student registration form");
			return "registration-form";
		} else {
			Student registeredStudent = studentService.registerStudent(student);
			if (registeredStudent.getId() != 0) {
				model.addAttribute("message", "Student Registered with ID " + registeredStudent.getId());
			} else {
				model.addAttribute("message", "Error in registering the student");
				return "registration-form";
			}
		}

		return "success";
	}
}
