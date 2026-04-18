package com.example.incidentplatform.config;


import io.swagger.v3.core.util.Json;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import tools.jackson.databind.ObjectMapper;

@EnableCaching
@Configuration
public class CacheConfig  {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Default configuration
        ObjectMapper mapper = new ObjectMapper();
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJacksonJsonRedisSerializer(mapper)))
                .disableCachingNullValues();

        // Per-cache configuration
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        // incidents cache - 60s TTL
        // incidents change a lot (status updates, assignments, etc)
        cacheConfigs.put("incidents", defaultConfig.entryTtl(Duration.ofSeconds(60)));

        cacheConfigs.put("incident-lists", defaultConfig.entryTtl(Duration.ofSeconds(30)));

        cacheConfigs.put("users", defaultConfig.entryTtl(Duration.ofMinutes(30)));

        cacheConfigs.put("user-lists", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        // Session cache - 1 hour TTL
        // TODO: set up sessions
        cacheConfigs.put("sessions", defaultConfig.entryTtl(Duration.ofHours(1)));

        // Short-lived cache - 1 minute TTL
        cacheConfigs.put("shortLived", defaultConfig.entryTtl(Duration.ofMinutes(1)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .transactionAware()
                .build();
    }
}
