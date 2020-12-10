package com.e3mall.freemaker;

import java.util.Date;

public class Student {

	private String name;
	private int age;
	private String address;
	private Date birth;
	
	public Student(String name,int age,String address,Date birth) {
		this.name = name;
		this.age = age;
		this.address = address;
		this.birth = birth;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}
