package com.jemhs.project.service;

public interface ReCaptchaService {

    boolean isResponseValid(String remoteIp,String response);

}