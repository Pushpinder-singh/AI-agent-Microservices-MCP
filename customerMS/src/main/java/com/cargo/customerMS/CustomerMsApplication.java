package com.cargo.customerMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerMsApplication {

    public static void main(String[] args) {
        System.err.println("Smart Cargo Customer MS started");
        SpringApplication.run(CustomerMsApplication.class, args);
    }

}
