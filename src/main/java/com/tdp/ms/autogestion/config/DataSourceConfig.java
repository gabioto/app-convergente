package com.tdp.ms.autogestion.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	@Bean(name = "dataSource")
	public DataSource dataSource() throws Exception {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		DataConnection datosConexion = new DataConnection();
		dataSource.setDriverClassName(datosConexion.getDriverclassname());
		dataSource.setUrl(datosConexion.getUrl());
		dataSource.setUsername(datosConexion.getUsername());
		dataSource.setPassword(datosConexion.getPassword());
		return dataSource;
	}

}
