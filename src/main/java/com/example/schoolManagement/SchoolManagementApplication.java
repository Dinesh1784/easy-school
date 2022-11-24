package com.example.schoolManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.schoolManagement.repository")
@EntityScan("com.example.schoolManagement.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class SchoolManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementApplication.class, args);
	}
}