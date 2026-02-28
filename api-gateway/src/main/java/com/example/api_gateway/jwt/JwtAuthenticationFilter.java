package com.example.api_gateway.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class JwtAuthenticationFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        String normalizedPath = path.endsWith("/") && path.length() > 1
                ? path.substring(0, path.length() - 1)
                : path;

        System.out.println("API Gateway → " + method + " " + path);

        // 🔓 EXCLUIR SWAGGER COMPLETO
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/webjars")
                || path.startsWith("/v3/api-docs")
                || path.contains("/v3/api-docs")) {
            return chain.filter(exchange);
        }

        // 🔓 Login público
        if ((normalizedPath.equals("/ms-personas/personas")
                || normalizedPath.equals("/ms-personas/personas/login"))
                && HttpMethod.POST.equals(method)) {
            return chain.filter(exchange);
        }

        // 🔓 PRODUCTOS PUBLICOS
        if (HttpMethod.GET.equals(method) && path.startsWith("/ms-productos")) {
            return chain.filter(exchange);
        }

        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateToken(token);

            Long personaId = Long.parseLong(claims.get("personaId").toString());
            String username = claims.getSubject();
            String rol = claims.get("rol").toString();

            // Construimos el request mutado con todos los headers necesarios
            ServerHttpRequest.Builder mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Username", username)
                    .header("X-Rol", rol)
                    .header("X-Persona-Id", String.valueOf(personaId));

            return chain.filter(exchange.mutate().request(mutatedRequest.build()).build());

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}