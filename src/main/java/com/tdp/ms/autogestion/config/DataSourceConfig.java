package com.tdp.ms.autogestion.config;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

	@Bean
    public DataSource getDataSource() {		
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.postgresql.Driver");//System.getenv("TDP_DRIVERCLASSNAME"));
		dataSourceBuilder.url("jdbc:postgresql://1ca72738-f3c8-4479-a5a4-e693416200d8.b2b5a92ee2df47d58bad0fa448c15585.databases.appdomain.cloud:31174/ibmclouddb");//System.getenv("TDP_URL"));
		dataSourceBuilder.username("user_averia");//System.getenv("TDP_USERNAME"));
		dataSourceBuilder.password("SyDe2VrKZH");//System.getenv("TDP_PASSWORD"));
        return dataSourceBuilder.build();
    }
	
}
