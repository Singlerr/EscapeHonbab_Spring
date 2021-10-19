package io.github.escapehonbab;

import io.github.escapehonbab.netty.MatchingServerBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EscapeHonbabApplication {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 1300;

    public static void main(String[] args) {
        SpringApplication.run(EscapeHonbabApplication.class, args);
        MatchingServerBootstrap.getInstance(HOST, PORT).startServer();
    }

}
