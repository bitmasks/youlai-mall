package com.youlai.common.security.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class OAuth2AuthenticationSerializer extends StdSerializer<OAuth2Authentication> {


    protected OAuth2AuthenticationSerializer() {
        super(OAuth2Authentication.class);
    }

    @Override
    public void serializeWithType(OAuth2Authentication value, JsonGenerator gen, SerializerProvider serializers,
                                  TypeSerializer typeSer) throws IOException {
        System.out.println("序列化2");
    }

    @Override
    public void serialize(OAuth2Authentication oAuth2Authentication, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        System.out.println("序列化");
    }
}
