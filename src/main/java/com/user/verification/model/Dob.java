package com.user.verification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dob {
    private String date;
    private int age;
    
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

   
}
