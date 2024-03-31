package com.user.verification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Results {
    private String gender;
    private UserName name;
    private String nat;
    private Dob dob;

    public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public UserName getName() {
		return name;
	}
	public void setName(UserName name) {
		this.name = name;
	}
	public String getNat() {
		return nat;
	}
	public void setNat(String nat) {
		this.nat = nat;
	}
	public Dob getDob() {
		return dob;
	}
	public void setDob(Dob dob) {
		this.dob = dob;
	}
    
    
}
