package com.user.verification.pagination;

import java.util.List;

import java.util.Map;

public class CustomPage<T> {
    private List<T> content;
    private Map<String, Object> pageInfo;
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public Map<String, Object> getpageInfo() {
		return pageInfo;
	}
	public void setpageInfo(Map<String, Object> pageInfo) {
		this.pageInfo = pageInfo;
	}
	public CustomPage() {
		super();
		
	}
}
