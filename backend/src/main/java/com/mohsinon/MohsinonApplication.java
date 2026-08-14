package com.mohsinon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MohsinonApplication {

    public static void main(String[] args) {
        SpringApplication.run(MohsinonApplication.class, args);
    }
}
