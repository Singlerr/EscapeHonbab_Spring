package io.github.escapehonbab;

import io.github.escapehonbab.netty.MatchingServerBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EscapeHonbabApplication {

    private static final String HOST = "localhost";
    private static final int PORT = 1300;

    public static void main(String[] args) {
        /*String apiKey = args[0];
        String apiSecret = args[1];*/
        //DatabaseHandler.getInstance().initializeDatabase();
        SpringApplication.run(EscapeHonbabApplication.class, args);
        MatchingServerBootstrap.getInstance(HOST, PORT).startServer();
    }

}
