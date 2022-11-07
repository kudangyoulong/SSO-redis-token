package com.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.sso.filter")
public class SsoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoTestApplication.class, args);
        System.out.println("Springboot---Starting.....");
    }

}
