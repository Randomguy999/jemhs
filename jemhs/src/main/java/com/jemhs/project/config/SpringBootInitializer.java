package com.jemhs.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@ComponentScan(basePackages="com.jemhs.project")
@EntityScan("com.jemhs.project")
@EnableJpaRepositories("com.jemhs.project")
public class SpringBootInitializer extends SpringBootServletInitializer {

	@Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;
 
    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;
    
	private static final Logger logger = LoggerFactory.getLogger(SpringBootInitializer.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringBootInitializer.class, args);
		System.out.println("Beans provided by Spring Boot:");

		/*String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			logger.info("Bean Names----" + beanName);
		}*/
	}
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootInitializer.class);
    }
	
	@Bean
	public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	templateEngine.setTemplateResolver(templateResolver);
	templateEngine.addDialect(new SpringSecurityDialect());
	return templateEngine;
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@Bean
	public AmazonS3Client amazonS3Client(AWSCredentials awsCredentials) {
		AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
	//	amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
		return amazonS3Client;
	}
	
	@Bean
	public AmazonSimpleEmailServiceClient amazonSESClient(AWSCredentials awsCredentials){
		AmazonSimpleEmailServiceClient awsSesClient = new AmazonSimpleEmailServiceClient(awsCredentials);
		return awsSesClient;
	}
	
}