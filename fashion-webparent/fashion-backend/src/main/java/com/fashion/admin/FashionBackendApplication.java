package com.fashion.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({ "com.fashion.admin.*", "com.fashion.admin" })
@EnableJpaRepositories(basePackages = { "com.fashion.admin.*" })
@EntityScan({ "com.fashion.fashioncommon.*" })
public class FashionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionBackendApplication.class, args);
	}

}

