package com.wevserver.application;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@ComponentScan("com.wevserver")
@EnableJdbcRepositories("com.wevserver.application")
public abstract class BaseApplication {

    protected static Class<?> primarySource;
    protected static ConfigurableApplicationContext context;

    public static Thread restartApplication() {

        final ApplicationArguments args = context.getBean(ApplicationArguments.class);

        final Thread thread =
                new Thread(
                        () -> {
                            context.close();
                            context = SpringApplication.run(primarySource, args.getSourceArgs());
                        },
                        "restartedMain");

        thread.setDaemon(false);
        thread.start();

        return thread;
    }
}
