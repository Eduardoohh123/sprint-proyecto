package com.example.prueba_sprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.example.prueba_sprint.repository", "dao"})
public class PruebaSprintApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaSprintApplication.class, args);
	}

}
