package com.wiki.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DevWikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevWikiApplication.class, args);
	}

}
