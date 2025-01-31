package com.wevserver.proxyserver;

import com.wevserver.application.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.wevserver")
public class ProxyServerApplication extends BaseApplication {

    public static void main(String[] args) {

        primarySource = ProxyServerApplication.class;

        SpringApplication.run(ProxyServerApplication.class, args);
    }
}
