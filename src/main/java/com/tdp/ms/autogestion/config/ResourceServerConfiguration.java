//package com.tdp.ms.autogestion.config;
//
//import java.util.Arrays;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// 
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
//public class ResourceServerConfiguration extends GlobalMethodSecurityConfiguration {
//
//	  @Configuration
//	  public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//		  
//		  @Override
//		    public void configure(HttpSecurity http) throws Exception {
//
//			  //http.authorizeRequests().antMatchers("/login*").permitAll();			  
//		        http.authorizeRequests().antMatchers(HttpMethod.GET, " /**").permitAll()
//		                .antMatchers(HttpMethod.POST, "/**").permitAll()
//		                .antMatchers(HttpMethod.PUT, "/**").permitAll()
//		                .antMatchers(HttpMethod.PATCH, "/**").permitAll()
//		                .antMatchers(HttpMethod.DELETE, "/**").permitAll();
//		    }
//		  
//		  @Bean
//	        CorsConfigurationSource corsConfigurationSource() {
//	            CorsConfiguration configuration = new CorsConfiguration();
//	            configuration.setAllowedOrigins(Arrays.asList("*"));
//	            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","HEAD","OPTIONS"));
//	            configuration.setAllowedHeaders(Arrays.asList("*"));
//	            configuration.setExposedHeaders(Arrays.asList("id_captcha","download_file_name"));
//	            
//	            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	            source.registerCorsConfiguration("/**", configuration);
//	            return source;
//	        }
//	  }
//	
//}
