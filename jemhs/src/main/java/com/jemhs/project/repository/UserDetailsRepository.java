package com.jemhs.project.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jemhs.project.model.UserDetails;

@Transactional
public interface UserDetailsRepository extends JpaRepository<UserDetails, String>{

	@Query(nativeQuery = true,value="SELECT DISTINCT user_registration.first_name,user_registration.email,"
			+ "user.user_name FROM user_registration  JOIN user ON user_registration.email = user.email"
			+ " WHERE user_registration.email=?1")
         UserDetails findByEmail(String email);
}
