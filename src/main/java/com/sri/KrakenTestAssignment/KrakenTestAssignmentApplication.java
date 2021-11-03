package com.sri.KrakenTestAssignment;

import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class KrakenTestAssignmentApplication {
	Logger logger = LoggerFactory.getLogger(KrakenTestAssignmentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KrakenTestAssignmentApplication.class, args);
	}
}
