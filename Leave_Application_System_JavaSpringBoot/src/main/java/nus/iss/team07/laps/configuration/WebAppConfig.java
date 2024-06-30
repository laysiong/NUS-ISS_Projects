package nus.iss.team07.laps.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nus.iss.team07.laps.interceptor.SecurityInterceptor;


@Component
public class WebAppConfig implements WebMvcConfigurer {
	  @Autowired
	  SecurityInterceptor securityInterceptor;
	  
	  @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(securityInterceptor)
	    		.excludePathPatterns("/css/**", "/image/**", "/", "/login/authenticate", "/api/holiday/**");
	  }
}


