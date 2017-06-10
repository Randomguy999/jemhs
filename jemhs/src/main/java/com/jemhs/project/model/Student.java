package com.jemhs.project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Entity
@Data
public class Student implements Serializable {

	private static final long serialVersionUID = -7228032663415896747L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Pattern(regexp = "^[A-Za-z ]{0,20}", message = "Enter only alphabets")
	private String firstName;
	@Pattern(regexp = "^[A-Za-z ]{0,20}", message = "Enter only alphabets")
	private String lastName;
	private String middleName;
	private String gender;
	@javax.validation.constraints.Pattern(regexp = "^[0-9 /]+", message = "Enter valid date of birth")
	private String dob;

	@javax.validation.constraints.Pattern(regexp = "^[0-9]{10}", message = "Enter 10 digit phone number")
	private String phone;

	@javax.validation.constraints.Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Enter valid email")
	private String email;
	private String address;
	private String standard;
	private String guardian;
	private String relation;
	@Transient
	private ArrayList<String> standardList;

	public Student() {

		standardList = new ArrayList<String>();

		standardList.add("I");
		standardList.add("II");
		standardList.add("III");
		standardList.add("IV");
		standardList.add("V");
		standardList.add("VI");
		standardList.add("VII");
		standardList.add("VIII");
		standardList.add("IX");
		standardList.add("X");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getGuardian() {
		return guardian;
	}

	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public ArrayList<String> getStandardList() {
		return standardList;
	}

	public void setStandardList(ArrayList<String> standardList) {
		this.standardList = standardList;
	}

}
