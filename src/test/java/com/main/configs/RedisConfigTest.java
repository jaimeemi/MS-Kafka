package com.main.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RedisConfigTest {

    @Test
    @DisplayName("jedisConnectionFactory - debe crear JedisConnectionFactory con localhost")
    void jedisConnectionFactory() {
        RedisConfig config = new RedisConfig();

        JedisConnectionFactory factory = config.jedisConnectionFactory("localhost", 6379);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("jedisConnectionFactory - debe crear con host y puerto personalizados")
    void jedisConnectionFactoryCustom() {
        RedisConfig config = new RedisConfig();

        JedisConnectionFactory factory = config.jedisConnectionFactory("redis-server", 6380);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("redisTemplate - debe crear RedisTemplate con serializadores")
    void redisTemplate() {
        RedisConfig config = new RedisConfig();
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        RedisTemplate<String, Object> template = config.redisTemplate(connectionFactory);

        assertNotNull(template);
        assertNotNull(template.getKeySerializer());
        assertNotNull(template.getValueSerializer());
        assertTrue(template.getKeySerializer() instanceof StringRedisSerializer);
        assertTrue(template.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer);
    }

    @Test
    @DisplayName("redisTemplate - debe configurar connectionFactory correctamente")
    void redisTemplateConnectionFactory() {
        RedisConfig config = new RedisConfig();
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        RedisTemplate<String, Object> template = config.redisTemplate(connectionFactory);

        assertNotNull(template);
        assertEquals(connectionFactory, template.getConnectionFactory());
    }
}
