package com.youlai.auth.security.serializer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import java.io.IOException;
import java.util.*;

public class AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

    protected AccessTokenJackson2Deserializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public OAuth2AccessToken deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        String tokenValue = jsonNode.get(OAuth2AccessToken.ACCESS_TOKEN).asText();
        String tokenType = jsonNode.get(OAuth2AccessToken.TOKEN_TYPE).asText();
        String refreshToken = jsonNode.get(OAuth2AccessToken.REFRESH_TOKEN).asText();
        long expiresIn = jsonNode.get(OAuth2AccessToken.EXPIRES_IN).asLong();
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(tokenValue);
        accessToken.setTokenType(tokenType);
        accessToken.setExpiration(new Date(System.currentTimeMillis() + expiresIn * 1000));
        if (refreshToken != null) {
            accessToken.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
        }
        // scope
        JsonNode scopeNode = jsonNode.get(OAuth2AccessToken.SCOPE);
        accessToken.setScope(parseScope(scopeNode));
        return accessToken;
    }


    private static Set<String> parseScope(JsonNode scopeNode) {
        // 空处理
        if (scopeNode == null || scopeNode.isNull()) {
            return Collections.emptySet();
        }
        // 字符串 x xx xxx 格式
        if (scopeNode.isTextual()) {
            return OAuth2Utils.parseParameterList(scopeNode.asText());
        }
        // 集合模型
        Set<String> scope = new TreeSet<>();
        Iterator<JsonNode> elements = scopeNode.getElements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            scope.add(next.asText());
        }
        return scope;
    }
}
