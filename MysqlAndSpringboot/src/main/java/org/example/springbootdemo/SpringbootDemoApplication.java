package org.example.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDemoApplication {
    public static void main(String[] args) {
        System.out.println("doing");
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }
}