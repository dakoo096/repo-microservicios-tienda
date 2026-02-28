package com.example.api_gateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class CustomJwtConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        String rol = jwt.getClaimAsString("rol");

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + rol);

        return Mono.just(
                new JwtAuthenticationToken(jwt, Collections.singleton(authority))
        );
    }
}