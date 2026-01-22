package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EnableRedisRepositories(basePackages = "com.example.demo.redis") // empty package
public class RepositoryConfig {
}