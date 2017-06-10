package com.jemhs.project.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jemhs.project.model.StudentCredentials;

@Transactional
public interface StudentCredentialsDao extends JpaRepository<StudentCredentials, Long>{

	StudentCredentials findByuserName(String userName);
	
	@Modifying
	@Query("update StudentCredentials set password = ?1")
	int changepassword(String password);
}
