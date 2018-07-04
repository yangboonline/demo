package com.bert.oauth2.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangbo
 * @date 2018/7/4
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedissonProperties redissonProperties;

    /**
     * 单机模式自动装配
     */
    @Bean
    public RedissonClient redissonSingle() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redissonProperties.address)
                .setTimeout(redissonProperties.timeout)
                .setConnectionPoolSize(redissonProperties.connectionPoolSize)
                .setConnectionMinimumIdleSize(redissonProperties.connectionMinimumIdleSize);
        return Redisson.create(config);
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "redisson")
    public class RedissonProperties {

        private int timeout = 3000;
        private String address;
        private String password;
        private int database = 0;
        private int connectionPoolSize = 64;
        private int connectionMinimumIdleSize = 10;
        private int slaveConnectionPoolSize = 250;
        private int masterConnectionPoolSize = 250;
        private String[] sentinelAddresses;
        private String masterName;

    }

}