package io.github.escapehonbab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("conf.properties")
@SpringBootApplication
public class EscapeHonbabApplication {

    private static final String HOST = "127.0.0.1";
    private static final int M_PORT = 1300;
    private static final int C_PORT = 1301;

    public static void main(String[] args) {
        SpringApplication.run(EscapeHonbabApplication.class, args);
    }


}
