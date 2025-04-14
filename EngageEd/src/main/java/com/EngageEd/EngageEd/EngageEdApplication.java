package com.EngageEd.EngageEd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.EngageEd.EngageEd")
public class EngageEdApplication {

	public static void main(String[] args) {
		SpringApplication.run(EngageEdApplication.class, args);
	}

}
