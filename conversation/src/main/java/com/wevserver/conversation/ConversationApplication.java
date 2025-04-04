package com.wevserver.conversation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan("com.wevserver")
@EnableJdbcRepositories({"com.wevserver.conversation"})
public class ConversationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConversationApplication.class, args);
    }
}
