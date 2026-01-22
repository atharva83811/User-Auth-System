package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

		config.setHostName("strong-roughy-27387.upstash.io");
		config.setPort(6379);
		config.setPassword("AWr7AAIncDEzMWIwOGNjOWVmYmU0M2QwOTI0Y2MyMDMxOWE2NDliM3AxMjczODc");

		return new LettuceConnectionFactory(config);
	}

	@Bean
	public StringRedisTemplate redisTemplate(LettuceConnectionFactory factory) {
		return new StringRedisTemplate(factory);
	}
}
