package com.user.verification.CustomException;

import java.time.LocalDateTime;


public class CustomError {
	private int code;
    private String message;
    private LocalDateTime timestamp;

    public CustomError(int code, String message, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
    
	        
	        
	    }


