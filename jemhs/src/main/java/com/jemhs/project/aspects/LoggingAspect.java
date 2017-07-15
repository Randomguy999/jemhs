package com.jemhs.project.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("execution(* com.jemhs.project.repository.*.*(..))")
	private void forRepositoryPackage() {}
	
	@Pointcut("execution(* com.jemhs.project.service.*.*(..))")
	private void forServicePackage() {}
	
	@Before("forRepositoryPackage()")
	public void beforeRepositoryAdvice() {		
		logger.info("\n=====>>>:::::::: Executing @Before advice on repository package :::::::::");		
	}
	
	@Before("forServicePackage()")
	public void beforeServiceAdvice() {		
		logger.info("\n=====>>>:::::::: Executing @Before advice on service package :::::::::");		
	}
}

