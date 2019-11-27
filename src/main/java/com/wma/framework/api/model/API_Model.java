package com.wma.framework.api.model;

public class API_Model {
	private String url;
	private String method;
	private String request;
	private String response;
	
	public API_Model(String url, String method, String request, String response) {
		this.url = url;
		this.method = method;
		this.request = request;
		this.response = response;		
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getRequest() {
		return request;
	}
	
	public String getResponse() {
		return url;
	}
	
	public String toString() {
		return "[--URL--:" + url + "]\n"
			+ "[--Method--:" + method + "]\n"
			+ "[--Request--:" + request + "]\n"
			+ "[--Response--:" + response + "]\n";
	}

}