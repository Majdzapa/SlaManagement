package com.company.sla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SlaManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlaManagementApplication.class, args);
	}

}
