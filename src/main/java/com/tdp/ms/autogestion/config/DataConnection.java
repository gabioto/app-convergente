package com.tdp.ms.autogestion.config;

public class DataConnection {

	private String driverclassname;
	private String url;
	private String username;
	private String password;

	public DataConnection() throws Exception {
		this.url = "jdbc:postgresql://1ca72738-f3c8-4479-a5a4-e693416200d8.b2b5a92ee2df47d58bad0fa448c15585.databases.appdomain.cloud:31174/ibmclouddb";//System.getenv("TDP_URL");
		this.driverclassname = "org.postgresql.Driver";//System.getenv("TDP_DRIVERCLASSNAME");
		this.username = "user_averia"; //System.getenv("TDP_USERNAME");
		this.password = "SyDe2VrKZH"; //System.getenv("TDP_PASSWORD");
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
