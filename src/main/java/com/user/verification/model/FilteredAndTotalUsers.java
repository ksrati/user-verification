package com.user.verification.model;

import java.util.List;

public class FilteredAndTotalUsers {
	 private List<User> filteredUsers;
	    private int totalDataCount;

	    // Constructor
	    public FilteredAndTotalUsers(List<User> filteredUsers, int totalDataCount) {
	        this.filteredUsers = filteredUsers;
	      
	        this.totalDataCount = totalDataCount;
	    }

		public List<User> getFilteredUsers() {
			return filteredUsers;
		}

		public void setFilteredUsers(List<User> filteredUsers) {
			this.filteredUsers = filteredUsers;
		}

		

		public int getTotalDataCount() {
			return totalDataCount;
		}

		public void setTotalDataCount(int totalDataCount) {
			this.totalDataCount = totalDataCount;
		}
	    
	    

}
