package com.example.gitrajabi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GitrajabiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitrajabiApplication.class, args);
    }

}
