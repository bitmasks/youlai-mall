package com.youlai.auth.security.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * AccessTokenMixIn
 *
 * @author dream.lu
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")

@JsonDeserialize(using = AccessTokenJackson2Deserializer.class)
@JsonSerialize(using = AccessTokenJackson2Serializer.class)
public class AccessTokenMixIn {

}
