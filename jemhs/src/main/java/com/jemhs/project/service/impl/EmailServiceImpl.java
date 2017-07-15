package com.jemhs.project.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.jemhs.project.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${mail.from}")
    private String FROM;
	
	@Autowired
	AmazonSimpleEmailServiceClient awsSesClient;
	public void sendSimpleMessage(String toUser, String subject, String message) {
		
		Destination destination = new Destination().withToAddresses(new String[] { toUser });
		// Create the subject and body of the message.
		Content content = new Content().withData(subject);
		Content textBody = new Content().withData(message);
		Body body = new Body().withHtml(textBody);

		// Create a message with the specified subject and body.
		Message msg = new Message().withSubject(content).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination)
				.withMessage(msg);

		try {
			logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			try {
				Region REGION = Region.getRegion(Regions.US_EAST_1);
				awsSesClient.setRegion(REGION);

				awsSesClient.sendEmail(request);
				logger.info("Email sent!");
			} catch (Exception e) {
				throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
						+ "Please make sure that your credentials file is at the correct "
						+ "location (~/.aws/credentials), and is in valid format.", e);
			}

			// Instantiate an Amazon SES client, which will make the service
			// call with the supplied AWS credentials.

			

		} catch (Exception ex) {
			logger.error("Error while sending email...");
			logger.error("Error message: " + ex.getMessage());

		}
	}

}
