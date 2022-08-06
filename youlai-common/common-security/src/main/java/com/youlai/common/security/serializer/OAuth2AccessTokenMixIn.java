package com.youlai.common.security.serializer;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSerialize(using = OAuth2AccessTokenJackson2Serializer.class)
@JsonDeserialize(using = OAuth2AccessTokenJackson2Deserializer.class)
public class OAuth2AccessTokenMixIn {
}
