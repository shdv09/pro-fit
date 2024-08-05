package com.shdv09.reportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ReportServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(ReportServiceApp.class, args);
	}

}
