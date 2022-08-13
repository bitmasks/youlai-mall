package com.youlai.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnMissingBean(SecurityConfiguration.class)
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain() {
        return null;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return null;
    }
}
