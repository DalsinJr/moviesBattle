package com.ada.moviesbattle.configuration;

import com.ada.moviesbattle.security.domain.dto.UserDTO;
import com.ada.moviesbattle.security.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private final UserService userService;

    public AppConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    @Bean
    public CommandLineRunner consoleRunner( ) {
        return args -> {
            userService.addUser(new UserDTO("user1", "user1"));
            userService.addUser(new UserDTO("user2", "user2"));

        };
    }
}