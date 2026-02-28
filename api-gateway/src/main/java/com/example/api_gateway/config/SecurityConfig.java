package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeExchange(exchange -> exchange

                        // 🔓 OPTIONS libre
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()

                        // 🔓 SWAGGER
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/ms-personas/v3/api-docs/**",
                                "/ms-productos/v3/api-docs/**",
                                "/ms-ventas/v3/api-docs/**",
                                "/ms-carrito/v3/api-docs/**"
                        ).permitAll()

                        // 🔓 LOGIN / REGISTRO
                        .pathMatchers("/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/ms-personas/personas").permitAll()
                        .pathMatchers(HttpMethod.POST, "/ms-personas/personas/login").permitAll()

                        // 🔓 PRODUCTOS PUBLICOS
                        .pathMatchers(HttpMethod.GET, "/ms-productos/productos/**").permitAll()

                        // 🔐 REGLAS DE NEGOCIO
                        .pathMatchers(HttpMethod.GET, "/ms-personas/personas")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.POST, "/ms-ventas/ventas")
                        .hasAnyRole("CLIENTE", "ADMIN")

                        .pathMatchers(HttpMethod.GET, "/ms-ventas/ventas/mis-ventas")
                        .authenticated()

                        .pathMatchers(HttpMethod.GET, "/ms-ventas/ventas")
                        .hasRole("ADMIN")

                        // 🔐 TODO LO DEMÁS
                        .anyExchange().authenticated()
                )

                // 🔐 JWT
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(new CustomJwtConverter())
                        )
                )

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String secret = "mi_clave_super_secreta_muy_larga_123456";
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}