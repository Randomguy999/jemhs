package com.jemhs.project.controller;

import com.jemhs.project.model.Admin;
import com.jemhs.project.service.AdminService;
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



@Controller
public class AdminController
{
  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
  @Autowired
  AdminService adminService;
  
  public AdminController() {}
  
  @InitBinder
  public void initBinder(WebDataBinder dataBinder) {
    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    
    dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
  }
  
  @RequestMapping({"/adminlogin"})
  public String showAdminLoginForm(Model map)
  {
    map.addAttribute("admin", new Admin());
    return "admin-login";
  }
  
  @RequestMapping({"/adminchangepass"})
  public String showChangePassForm(ModelMap map) {
    logger.info("In showChangePassForm Method...");
    
    map.addAttribute("changePass", new Admin());
    return "admin-changepass";
  }
  
  @RequestMapping({"/adminloginProcess"})
  public String login(@Valid @ModelAttribute("admin") Admin admin, BindingResult theBindingResult, Model model) {
    boolean isValidUser = false;
    
    logger.info("Login request for user " + admin.getUserName());
    if (theBindingResult.hasErrors()) {
      logger.error("Errors in the submitted admin login form");
      return "admin-login";
    }
    isValidUser = adminService.isValidUser(admin);
    if (!isValidUser) {
      model.addAttribute("message", "Login Failed");
      return "admin-login";
    }
    model.addAttribute("message", "Login Success");
    

    return "admin-confirmation";
  }
  
  @RequestMapping({"/adminchangePassword"})
  public String changeAdminPassword(@Valid @ModelAttribute("changePass") Admin admin, BindingResult theBindingResult, Model model)
  {
    boolean updatedPass = false;
    
    logger.info("In Change password method");
    if (theBindingResult.hasErrors()) {
      logger.error("Errors in the submitted change Password form");
      return "admin-changepass";
    }
    updatedPass = adminService.changePassword(admin);
    if (!updatedPass) {
      model.addAttribute("message", "Enter correct Credentials");
      return "admin-changepass";
    }
    model.addAttribute("message", "Password changed successfully");
    

    return "success";
  }
}