package com.user.verification.model;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResult {
    private String gender;
    private String fullname;
    private String nat;
    private String dobDate;
    private List<Results> results;
    
    public List<Results> getResults() {
        return results;
    }
    
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setResults(List<Results> results) {
		this.results = results;
	}

	public String getNat() {
		return nat;
	}
	public void setNat(String nat) {
		this.nat = nat;
	}

	public String getDobDate() {
		return dobDate;
	}

	public void setDobDate(String dobDate) {
		this.dobDate = dobDate;
	}
	
    

}

