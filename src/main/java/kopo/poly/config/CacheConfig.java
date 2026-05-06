package kopo.poly.config;

import kopo.poly.util.DateUtil;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Configuration
public class CacheConfig {

    @Bean("melonKeyGen")
    public KeyGenerator melonKeyGen() {
        return (target, method, params) ->
                "MELON_" + DateUtil.getDateTime("yyyyMMdd");
    }

    @Bean
    RedisCacheManager redisCacheManager(RedisConnectionFactory cf) {
        var json = new GenericJackson2JsonRedisSerializer();
        var conf = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(json)
                )
                .entryTtl(Duration.ofHours(3))
                .disableCachingNullValues();

        return RedisCacheManager.builder(cf)
                .cacheDefaults(conf)
                .build();
    }
}
