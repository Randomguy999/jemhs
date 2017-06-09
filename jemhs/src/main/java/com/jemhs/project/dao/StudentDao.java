package com.jemhs.project.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jemhs.project.model.Student;

public interface StudentDao extends JpaRepository<Student, Long> {

//	@SuppressWarnings("unchecked")
//	Student save(Student studentDetails);
	
}
