package com.jemhs.project.service;

public abstract interface EmailService
{
  public  void sendSimpleMessage(String toUser, String subject, String body);
}