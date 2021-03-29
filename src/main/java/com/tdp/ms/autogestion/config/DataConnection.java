package com.tdp.ms.autogestion.config;

public class DataConnection {

	private String driverclassname;
	private String url;
	private String username;
	private String password;

	public DataConnection() throws Exception {
		this.url = System.getenv("TDP_URL");
		this.driverclassname = System.getenv("TDP_DRIVERCLASSNAME");
		this.username = System.getenv("TDP_USERNAME");
		this.password = System.getenv("TDP_PASSWORD");
	}

	public DataConnection(String driverclassname, String url, String username, String password) {
		super();
		this.driverclassname = driverclassname;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getDriverclassname() {
		return driverclassname;
	}

	public void setDriverclassname(String driverclassname) {
		this.driverclassname = driverclassname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
