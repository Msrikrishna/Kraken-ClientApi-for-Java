package com.sri.KrakenJavaClientAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class KrakenJavaClientAPI {
	Logger logger = LoggerFactory.getLogger(KrakenJavaClientAPI.class);

	public static void main(String[] args) {
		SpringApplication.run(KrakenJavaClientAPI.class, args);
	}
}
