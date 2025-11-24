package com.example.gitrajabi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.example.gitrajabi.config.CustomPrincipalArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import java.util.List;
@SpringBootApplication
@EnableJpaAuditing

public class GitrajabiApplication implements WebMvcConfigurer {

    private final CustomPrincipalArgumentResolver customPrincipalArgumentResolver;

    public GitrajabiApplication(CustomPrincipalArgumentResolver customPrincipalArgumentResolver) {
        this.customPrincipalArgumentResolver = customPrincipalArgumentResolver;
    }


    public static void main(String[] args) {
        SpringApplication.run(GitrajabiApplication.class, args);
    }
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customPrincipalArgumentResolver);
    }
}
