package org.example.springbootdemo;

import org.apache.catalina.util.ServerInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDemoApplication {
    public static void main(String[] args) {
        System.out.println("Tomcat version: " + ServerInfo.getServerInfo());
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }
}