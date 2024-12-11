package com.panyukovnn.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LinkShortenerApp {
	public static void main(String[] args) {
		SpringApplication.run(LinkShortenerApp.class);
	}
}
