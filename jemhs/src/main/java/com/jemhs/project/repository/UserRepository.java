package com.jemhs.project.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jemhs.project.model.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);
	
	@Modifying
	@Query("update User set password = ?1 where user_name=?2")
	int changepassword(String password,String userName);
}
