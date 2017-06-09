package com.jemhs.project.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Admin implements Serializable{

	private static final long serialVersionUID = -1800649707248682420L;

	@NotNull(message= "is required")
	@Id
	private String userName;
	
	@NotNull(message= "is required")
	private String password;
	
	@Transient
	private String newPassword;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "Admin [userName=" + userName + ", password=" + password + ", newPassword=" + newPassword + "]";
	}

	

}
