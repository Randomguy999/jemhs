package com.jemhs.project.service.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jemhs.project.service.ReCaptchaService;
import com.jemhs.project.util.ReCaptchaServiceException;

@Service
public class ReCaptchaServiceImpl implements ReCaptchaService {

	@Autowired
	RestTemplate restTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(ReCaptchaServiceImpl.class);
	
    private static class RecaptchaResponse {
        @JsonProperty("success")
        private boolean success;
        @JsonProperty("error-codes")
        private Collection<String> errorCodes;
    }
    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    @Value("${recaptcha.secret-key}")
    private String recaptchaSecretKey;

    
    @Override
    public boolean isResponseValid(String remoteIp, String response) {
        RecaptchaResponse recaptchaResponse;
        try {
            recaptchaResponse = restTemplate.postForEntity(
                    recaptchaUrl, createBody(recaptchaSecretKey, remoteIp, response), RecaptchaResponse.class)
                    .getBody();
        } catch (RestClientException e) {
            throw new ReCaptchaServiceException("Recaptcha API not available due to exception", e);
        }
        if (recaptchaResponse.success) {
            return true;
        } else {
            logger.debug("Unsuccessful recaptchaResponse={}", recaptchaResponse);
            return false;
        }
    }

    private MultiValueMap<String, String> createBody(String secret, String remoteIp, String response) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }

}