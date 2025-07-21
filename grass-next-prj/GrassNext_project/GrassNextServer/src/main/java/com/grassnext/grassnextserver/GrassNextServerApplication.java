package com.grassnext.grassnextserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main application class for the GrassNext Server application.
 *
 */
@SpringBootApplication
public class GrassNextServerApplication extends SpringBootServletInitializer {
    
    /**
     * Main method which starts the Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(GrassNextServerApplication.class, args);
    }
}