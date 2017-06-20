package com.jemhs.project.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages="com.jemhs.project")
@EntityScan("com.jemhs.project")
@EnableJpaRepositories("com.jemhs.project")
public class SpringBootInitializer extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootInitializer.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringBootInitializer.class, args);
		System.out.println("Beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			logger.info("Bean Names----" + beanName);
		}
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
}