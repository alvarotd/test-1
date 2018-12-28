package com.n26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MyConfiguration.class)
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

}
