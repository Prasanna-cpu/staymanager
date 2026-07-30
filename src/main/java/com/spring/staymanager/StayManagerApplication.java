package com.spring.staymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImplementation")
public class StayManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StayManagerApplication.class, args);
	}

}
