package com.youlai.auth.security.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.redis.BaseRedisTokenStoreSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

public class JacksonRedisTokenStoreSerializationStrategy extends BaseRedisTokenStoreSerializationStrategy {

    private final ObjectMapper objectMapper;

    public JacksonRedisTokenStoreSerializationStrategy() {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
        this.objectMapper.addMixIn(OAuth2AccessToken.class, AccessTokenMixIn.class);
        this.objectMapper.registerModule(new CoreJackson2Module());
    }


    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> aClass) {
        return null;
    }

    @Override
    protected String deserializeStringInternal(byte[] bytes) {
        return null;
    }

    @Override
    protected byte[] serializeInternal(Object o) {
        return new byte[0];
    }

    @Override
    protected byte[] serializeInternal(String s) {
        return new byte[0];
    }
}
