package com.tdp.ms.autogestion.config;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableTransactionManagement
public class DataSourceConfig {

//	@Bean(name = "dataSource")
//	public DataSource dataSource() throws Exception {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		DataConnection datosConexion = new DataConnection();
//		dataSource.setDriverClassName(datosConexion.getDriverclassname());
//		dataSource.setUrl(datosConexion.getUrl());
//		dataSource.setUsername(datosConexion.getUsername());
//		dataSource.setPassword(datosConexion.getPassword());
//		return dataSource;
//	}
	
	@Bean
    public DataSource getDataSource() {
		
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(System.getenv("TDP_DRIVERCLASSNAME"));
		dataSourceBuilder.url(System.getenv("TDP_URL"));
		dataSourceBuilder.username(System.getenv("TDP_USERNAME"));
		dataSourceBuilder.password(System.getenv("TDP_PASSWORD"));
//        dataSourceBuilder.driverClassName("org.postgresql.Driver");
//        dataSourceBuilder.url("jdbc:postgresql://a1f396ab-707f-4fcc-8225-fee7f1d7647a.br37s45d0p54n73ffbr0.databases.appdomain.cloud:30865/ibmclouddb");
//        dataSourceBuilder.username("user_averia");
//        dataSourceBuilder.password("SyDe2VrKZH");
        return dataSourceBuilder.build();
    }
	
}
