package com.jemhs.project.service;

import com.jemhs.project.model.Student;
import com.jemhs.project.model.StudentCredentials;

public abstract interface StudentService
{
  public abstract Student registerStudent(Student paramStudent);
  
  public abstract boolean isValidUser(StudentCredentials paramStudentCredentials);
  
  public abstract boolean changePassword(StudentCredentials paramStudentCredentials);
}