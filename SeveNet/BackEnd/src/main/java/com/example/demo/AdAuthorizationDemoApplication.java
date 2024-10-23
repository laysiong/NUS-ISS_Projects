package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class AdAuthorizationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdAuthorizationDemoApplication.class, args);
	}

}
