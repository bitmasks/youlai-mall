package com.youlai.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveRedisAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenStore redisTokenStore;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return null;
    }
}
