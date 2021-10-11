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
		dataSourceBuilder.driverClassName(System.getenv("TDP_DRIVERCLASSNAME"));
		dataSourceBuilder.url(System.getenv("TDP_URL"));
		dataSourceBuilder.username(System.getenv("TDP_USERNAME"));
		dataSourceBuilder.password(System.getenv("TDP_PASSWORD"));
        return dataSourceBuilder.build();
    }
	
}
