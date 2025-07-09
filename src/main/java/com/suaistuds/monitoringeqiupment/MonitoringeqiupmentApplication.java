package com.suaistuds.monitoringeqiupment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.suaistuds.monitoringeqiupment.repository")
public class MonitoringeqiupmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonitoringeqiupmentApplication.class, args);
	}
}
