package com.tdp.ms.autogestion.config;

public class DataConnection {

	private String driverclassname;
	private String url;
	private String username;
	private String password;
	
	public DataConnection() {
		
		this.url="jdbc:postgresql://sl-us-south-1-portal.54.dblayer.com:23239/compose";
		this.driverclassname ="org.postgresql.Driver";
		this.username="admin";
		this.password="GNTVLIQEAIPDVGTT";

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
