package com.jemhs.project.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jemhs.project.dao.StudentDao;
import com.jemhs.project.model.Student;
import com.jemhs.project.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Autowired
	StudentDao studentDao;

	@Override
	public Student registerStudent(Student student) {
		Student registeredStudent = null;
		try {
			registeredStudent = studentDao.save(student);
		} catch (Exception e) {
			logger.error("Error while registering student ...");
			e.printStackTrace();
		}
		return registeredStudent;

	}

}
