package com.advenio.medere.emr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@ComponentScan({"com.advenio.medere"})
@EntityScan({"com.advenio.medere.objects","com.advenio.medere.emr.objects"})
@EnableJpaRepositories({"com.advenio.medere.dao","com.advenio.medere.emr.dao"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
