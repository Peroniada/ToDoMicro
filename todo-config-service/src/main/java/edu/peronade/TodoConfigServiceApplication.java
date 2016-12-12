package edu.peronade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class TodoConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoConfigServiceApplication.class, args);
	}
}
