package com.jemhs.project.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserDetails {

	private String firstName;
	private String email;
	@Id
	private String userName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
