package com.user.verification.model;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserName {
    private String title;
    private String first;
    private String last;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first){
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}

    
}
