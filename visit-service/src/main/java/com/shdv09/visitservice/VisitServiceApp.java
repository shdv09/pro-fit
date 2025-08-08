package com.shdv09.visitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VisitServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(VisitServiceApp.class, args);
	}

}
