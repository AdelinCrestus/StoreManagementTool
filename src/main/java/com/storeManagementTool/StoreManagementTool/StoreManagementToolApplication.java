package com.storeManagementTool.StoreManagementTool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"com.storeManagementTool.StoreManagementTool.mappers"})
public class StoreManagementToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreManagementToolApplication.class, args);
	}

}
