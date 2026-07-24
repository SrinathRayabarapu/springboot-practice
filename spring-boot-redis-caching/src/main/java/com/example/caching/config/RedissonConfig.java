package com.example.caching.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties, RedissonProperties redissonProperties) {
        Config config = new Config();
        String address = resolveAddress(redisProperties, redissonProperties);
        config.useSingleServer()
                .setAddress(address)
                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());

        String password = firstNonBlank(redissonProperties.getPassword(), redisProperties.getPassword());
        if (StringUtils.hasText(password)) {
            config.useSingleServer().setPassword(password);
        }

        return Redisson.create(config);
    }

    private static String resolveAddress(RedisProperties redisProperties, RedissonProperties redissonProperties) {
        if (StringUtils.hasText(redissonProperties.getAddress())) {
            return redissonProperties.getAddress();
        }
        String host = StringUtils.hasText(redisProperties.getHost()) ? redisProperties.getHost() : "localhost";
        int port = redisProperties.getPort() > 0 ? redisProperties.getPort() : 6379;
        return (redisProperties.isSsl() ? "rediss://" : "redis://") + host + ":" + port;
    }

    private static String firstNonBlank(String primary, String fallback) {
        return StringUtils.hasText(primary) ? primary : fallback;
    }
}
