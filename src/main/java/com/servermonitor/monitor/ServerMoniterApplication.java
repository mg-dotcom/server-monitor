package com.servermonitor.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServerMoniterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerMoniterApplication.class, args);
    }
}
