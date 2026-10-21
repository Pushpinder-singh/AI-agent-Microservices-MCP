package com.cargo.customerMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CustomerMsApplication {

    public static void main(String[] args) {
        System.err.println("Smart Cargo Customer MS started");
        SpringApplication.run(CustomerMsApplication.class, args);
    }

}
