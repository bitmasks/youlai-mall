package com.youlai.common.security.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import java.io.IOException;
import java.util.*;

public class OAuth2AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

    protected OAuth2AccessTokenJackson2Deserializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
            throws IOException {
        return myDeserializeWithType(p);
    }

    @Override
    public OAuth2AccessToken deserialize(com.fasterxml.jackson.core.JsonParser jp, com.fasterxml.jackson.databind.DeserializationContext deserializationContext) throws IOException, JacksonException {
        return myDeserializeWithType(jp);
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
        Iterator<JsonNode> elements = scopeNode.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            scope.add(next.asText());
        }
        return scope;
    }

    private OAuth2AccessToken myDeserializeWithType(JsonParser jp) throws IOException {
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
        // 扩展字段
        accessToken.setAdditionalInformation(parseAdditionalInformation(mapper, jsonNode));
        return accessToken;
    }

    /**
     * 解析扩展字段
     *
     * @param mapper   jackson 工具
     * @param jsonNode json String
     * @return map
     */
    private Map<String, Object> parseAdditionalInformation(ObjectMapper mapper, JsonNode jsonNode) {
        LinkedHashMap<String, Object> additionalInformation = new LinkedHashMap<>();
        long deptId = jsonNode.get("deptId").asLong();
        long userId = jsonNode.get("userId").asLong();
        additionalInformation.put("deptId", deptId);
        additionalInformation.put("userId", userId);

        String authorities = jsonNode.get("authorities").asText();
        additionalInformation.put("authorities", authorities);
        return additionalInformation;
    }

}
