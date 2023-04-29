package com.fashion.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.fashion.client.*", "com.fashion.client" })
@EnableJpaRepositories(basePackages = { "com.fashion.client.*" })
@EntityScan({ "com.fashion.fashioncommon.*" })
public class FashionFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionFrontendApplication.class, args);
	}

}
