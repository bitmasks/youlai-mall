package com.youlai.gateway;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

public class JacksonSerializationStrategy extends StandardStringSerializationStrategy {

    private static final GenericJackson2JsonRedisSerializer JSON_SERIALIZER = new GenericJackson2JsonRedisSerializer();

    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        return JSON_SERIALIZER.deserialize(bytes,clazz);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        return JSON_SERIALIZER.serialize(object);
    }
}
