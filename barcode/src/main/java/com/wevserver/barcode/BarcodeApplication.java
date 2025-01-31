package com.wevserver.barcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.wevserver")
public class BarcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarcodeApplication.class, args);
    }
}
