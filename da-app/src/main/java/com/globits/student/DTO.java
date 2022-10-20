package com.globits.student;

public class DTO {
	String code;
	String name;
	int age;
	
	public DTO(String code, String name, int age) {
		super();
		this.code = code;
		this.name = name;
		this.age = age;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public DTO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DTO [code=" + code + ", name=" + name + ", age=" + age + "]";
	}
	

}
