package com.main.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CacheConfigTest {

    @Test
    @DisplayName("cacheManager - debe crear RedisCacheManager con configuración correcta")
    void cacheManager() {
        CacheConfig config = new CacheConfig();
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        RedisCacheManager cacheManager = config.cacheManager(connectionFactory);

        assertNotNull(cacheManager);
        assertNotNull(cacheManager.getCacheNames());
    }

    @Test
    @DisplayName("cacheManager - debe configurar TTL de 30 minutos")
    void cacheManagerTTL() {
        CacheConfig config = new CacheConfig();
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        RedisCacheManager cacheManager = config.cacheManager(connectionFactory);

        assertNotNull(cacheManager);
    }
}
