package com.tdp.ms.autogestion.config;

import com.tdp.ms.autogestion.util.SecurityUtil;

public class DataConnection {

	private String driverclassname;
	private String url;
	private String username;
	private String password;

	public DataConnection() throws Exception {
		// Test
//		this.url="jdbc:postgresql://1ca72738-f3c8-4479-a5a4-e693416200d8.b2b5a92ee2df47d58bad0fa448c15585.databases.appdomain.cloud:31174/ibmclouddb";
//		this.driverclassname ="org.postgresql.Driver";
//		this.username="user_averia";
//		this.password = SecurityUtil.decrypt("LJ93AUKHW6P9uYaupkaZEw==");
		// Dev
//		this.url="jdbc:postgresql://sl-us-south-1-portal.54.dblayer.com:23239/compose";
//		this.driverclassname ="org.postgresql.Driver";
//		this.username="admin";
//		this.password = SecurityUtil.decrypt("f97+11v5U9xxkkF/VOxNwY5FP8g5NlGrb7HyLq5D+uA=");
		// Cert
//		this.url = "jdbc:postgresql://a1f396ab-707f-4fcc-8225-fee7f1d7647a.br37s45d0p54n73ffbr0.databases.appdomain.cloud:30865/ibmclouddb";
//		this.driverclassname = "org.postgresql.Driver";
//		this.username = "user_averia";
//		this.password = SecurityUtil.decrypt("LJ93AUKHW6P9uYaupkaZEw==");
		// Prod
//		this.url = "jdbc:postgresql://6de706f1-ed6b-41c3-9714-d84afa6c2726.974550db55eb4ec0983f023940bf637f.databases.appdomain.cloud:31164/ibmclouddb";
//		this.driverclassname = "org.postgresql.Driver";
//		this.username = "user_averia";
//		this.password = SecurityUtil.decrypt("LJ93AUKHW6P9uYaupkaZEw==");
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
