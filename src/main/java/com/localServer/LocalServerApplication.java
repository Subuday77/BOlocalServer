package com.localServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LocalServerApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(LocalServerApplication.class, args);
		System.out.println("Server started.......");
	}

}
