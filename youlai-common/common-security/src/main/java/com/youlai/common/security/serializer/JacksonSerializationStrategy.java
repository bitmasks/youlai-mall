package com.youlai.common.security.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.BaseRedisTokenStoreSerializationStrategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JacksonSerializationStrategy extends BaseRedisTokenStoreSerializationStrategy {

    private final ObjectMapper objectMapper;

    public JacksonSerializationStrategy() {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
        this.objectMapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixIn.class);
        this.objectMapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMixin.class);
        this.objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
        this.objectMapper.registerModule(new CoreJackson2Module());
    }


    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String deserializeStringInternal(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    protected byte[] serializeInternal(Object o) {
        try {
            return objectMapper.writeValueAsBytes(o);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected byte[] serializeInternal(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }
}
