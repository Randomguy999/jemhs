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
public class Student implements Serializable{

	private static final long serialVersionUID = -7228032663415896747L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Pattern(regexp="^[A-Za-z ]{0,20}",message="Enter only alphabets")
	private String firstName;
	@Pattern(regexp="^[A-Za-z ]{0,20}",message="Enter only alphabets")
	private String lastName;
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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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

	public ArrayList<Integer> getDayList() {
		return dayList;
	}

	public void setDayList(ArrayList<Integer> dayList) {
		this.dayList = dayList;
	}

	public LinkedHashMap<Integer, String> getMonthList() {
		return monthList;
	}

	public void setMonthList(LinkedHashMap<Integer, String> monthList) {
		this.monthList = monthList;
	}

	public ArrayList<Integer> getYearList() {
		return yearList;
	}

	public void setYearList(ArrayList<Integer> yearList) {
		this.yearList = yearList;
	}

	public ArrayList<String> getStandardList() {
		return standardList;
	}

	public void setStandardList(ArrayList<String> standardList) {
		this.standardList = standardList;
	}

	private String middleName;
	private String gender;
	private String day;
	private String month;
	private String year;
	@Pattern(regexp="^[0-9]{10}", message="Enter 10 digit phone number")
	private String phone; 
	private String address;
	private String standard;
	private String guardian;
	private String relation;
	
	@Transient
	private ArrayList<Integer> dayList;
	@Transient
	private LinkedHashMap<Integer, String> monthList;
	@Transient
	private ArrayList<Integer> yearList;
	@Transient
	private ArrayList<String> standardList;
	
	public Student(){
		dayList = new ArrayList<Integer>();
		monthList =  new LinkedHashMap<Integer,String>();
		yearList = new ArrayList<Integer>();
		standardList = new ArrayList<String>();
		
		dayList.add(1);
		dayList.add(2);
		dayList.add(3);
		dayList.add(4);
		dayList.add(5);
		dayList.add(6);
		dayList.add(7);
		dayList.add(8);
		dayList.add(9);
		dayList.add(10);
		dayList.add(11);
		dayList.add(12);
		dayList.add(13);
		dayList.add(14);
		dayList.add(15);
		dayList.add(16);
		dayList.add(17);
		dayList.add(18);
		dayList.add(19);
		dayList.add(20);
		dayList.add(21);
		dayList.add(22);
		dayList.add(23);
		dayList.add(24);
		dayList.add(25);
		dayList.add(26);
		dayList.add(27);
		dayList.add(28);
		dayList.add(29);
		dayList.add(30);
		dayList.add(31);
		
		
		monthList.put(1,"January");
		monthList.put(2,"February");
		monthList.put(3,"March");
		monthList.put(4,"April");
		monthList.put(5,"May");
		monthList.put(6,"June");
		monthList.put(7,"July");
		monthList.put(8,"August");
		monthList.put(9,"September");
		monthList.put(10,"October");
		monthList.put(11,"November");
		monthList.put(12,"December");
		
		yearList.add(2000);
		yearList.add(2001);
		yearList.add(2002);
		yearList.add(2003);
		yearList.add(2004);
		yearList.add(2005);
		yearList.add(2006);
		yearList.add(2007);
		yearList.add(2008);
		yearList.add(2009);
		yearList.add(2010);
		
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
		
	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName="
				+ middleName + ", gender=" + gender + ", day=" + day + ", month=" + month + ", year=" + year
				+ ", phone=" + phone + ", address=" + address + ", standard=" + standard + ", guardian=" + guardian
				+ ", relation=" + relation + ", dayList=" + dayList + ", monthList=" + monthList + ", yearList="
				+ yearList + ", standardList=" + standardList + "]";
	}

}
