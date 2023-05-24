package io.project.ResumeProcessorBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("io.project.ResumeProcessorBot.repository")
public class ResumeProcessorBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeProcessorBotApplication.class, args);
	}

}
