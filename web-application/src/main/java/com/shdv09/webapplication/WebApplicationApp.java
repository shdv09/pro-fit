package com.shdv09.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WebApplicationApp {

	public static void main(String[] args) {
		SpringApplication.run(WebApplicationApp.class, args);
	}

}
