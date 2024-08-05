package com.shdv09.appointmentservice;

import com.shdv09.appointmentservice.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AppointmentServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApp.class, args);
	}

}
