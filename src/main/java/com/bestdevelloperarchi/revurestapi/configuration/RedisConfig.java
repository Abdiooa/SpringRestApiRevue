package com.bestdevelloperarchi.revurestapi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    /*
    * This bean creates a LettuceConnectionFactory, which is the connection
    *  factory for Redis based on the Lettuce library. It is used \
    * to connect to the Redis server using the host and port specified in the properties.
    * */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost,redisPort);
        return new LettuceConnectionFactory(configuration);
    }
    /*
    * This bean creates a RedisCacheManager, which manages the
    * Redis cache for the application. It uses the redisConnectionFactory to
    * establish a connection with the Redis server.

The method configures the cache manager with two named caches:
*  "employees" and "employee". Each cache is associated with a specific cache
* configuration.

The "employees" cache is configured to have a TTL of 1 minute
*  using the myDefaultCacheConfig(Duration.ofMinutes(1)) method.
The "employee" cache is also configured to have a TTL of 1 minute using the same method.
    * */
    @Bean()
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        RedisCacheConfiguration configuration = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(configuration)
                .withCacheConfiguration("employees",myDefaultCacheConfig(Duration.ofMinutes(5)))
                .withCacheConfiguration("employee",myDefaultCacheConfig(Duration.ofMinutes(1)))
                .build();
    }
    /*
    * This private method configures the default caching behavior. It sets
    *  the default time to live (TTL) for cache entries to 10 minutes using
    * Duration.ofMinutes(10). It also specifies that the values in the cache
    * should be serialized using Jackson's JSON serializer (GenericJackson2JsonRedisSerializer).
    *  This means that objects
    * will be converted to JSON format before storing them in the Redis cache.
    *
    * */
    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration){
        return  RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
    @EventListener(ContextClosedEvent.class)
    public void onApplicationShutdown() {
        // Optionally, you can clear the cache when the application is shutting down
        RedisCacheManager cacheManager = cacheManager(redisConnectionFactory());
        if (cacheManager != null) {
            cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        }
    }
}
