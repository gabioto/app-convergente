package com.tdp.ms.autogestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Clase de configuracion para rest OP.
 * @author Cesar Olivares
 * @version 1.0
 */

@EnableWebMvc
@Configuration
public class AutogestionConfig {

	@Bean
    public javax.validation.Validator validator() {
        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }

}
