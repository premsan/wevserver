package com.wevserver.wevserver;

import com.wevserver.application.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.wevserver")
public class WevServerApplication extends BaseApplication {

    public static void main(String[] args) {

        primarySource = WevServerApplication.class;
        context = SpringApplication.run(WevServerApplication.class, args);
    }
}
