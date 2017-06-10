package com.jemhs.project.controller;

import com.jemhs.project.model.Student;
import com.jemhs.project.model.StudentCredentials;
import com.jemhs.project.service.StudentService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class StudentController
{
  private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
  @Autowired
  StudentService studentService;
  
  public StudentController() {}
  
  @RequestMapping({"/studentlogin"})
  public String showAdminLoginForm(Model map) { logger.info("In Student Controller...::::::+++++");
    
    map.addAttribute("studentCred", new StudentCredentials());
    return "student-login";
  }
  
  @RequestMapping({"/showRegistrationForm"})
  public String showStudentRegistrationForm(Model map) {
    logger.info("In Show Student Registration method...");
    
    map.addAttribute("studentRegistration", new Student());
    return "registration-form";
  }
  
  @RequestMapping({"/studentchangepass"})
  public String showChangePassForm(ModelMap map) {
    logger.info("In showChangePassForm Method...");
    
    map.addAttribute("changePass", new StudentCredentials());
    return "student-changepass";
  }
  


  @RequestMapping({"/RegistrationProcess"})
  public String login(@Valid @ModelAttribute("studentRegistration") Student student, BindingResult theBindingResult, Model model)
  {
    logger.info("Student Details :::: " + student);
    if (theBindingResult.hasErrors()) {
      logger.error("Errors in the submitted student registration form");
      return "registration-form";
    }
    Student registeredStudent = studentService.registerStudent(student);
    if (registeredStudent.getId() != 0) {
      model.addAttribute("message", "Student Registered with ID " + registeredStudent.getId());
    } else {
      model.addAttribute("message", "Error in registering the student");
      return "registration-form";
    }
    

    return "success";
  }
  
  @RequestMapping({"/studentloginProcess"})
  public String login(@Valid @ModelAttribute("studentCred") StudentCredentials student, BindingResult theBindingResult, Model model) {
    boolean isValidUser = false;
    
    if (theBindingResult.hasErrors()) {
      logger.error("Errors in the submitted student login form");
      return "student-login";
    }
    isValidUser = studentService.isValidUser(student);
    if (!isValidUser) {
      model.addAttribute("message", "Login Failed");
      return "student-login";
    }
    model.addAttribute("message", "Login Success");
    

    return "admin-confirmation";
  }
  

  @RequestMapping({"/studentchangePassword"})
  public String changeAdminPassword(@Valid @ModelAttribute("changePass") StudentCredentials student, BindingResult theBindingResult, Model model)
  {
    boolean updatedPass = false;
    
    if (theBindingResult.hasErrors()) {
      logger.error("Errors in the submitted change Password form");
      return "student-changepass";
    }
    updatedPass = studentService.changePassword(student);
    if (!updatedPass) {
      model.addAttribute("message", "Enter correct Credentials");
      return "student-changepass";
    }
    model.addAttribute("message", "Password changed successfully");
    

    return "success";
  }
}