package com.example.api_gateway.controller;

import com.example.api_gateway.dto.PersonaResponse;
import com.example.api_gateway.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final WebClient webClient;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> request) {

        return webClient.post()
                .uri("http://ms-personas/personas/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PersonaResponse.class)
                .map(persona -> {

                    String token = jwtUtil.generateToken(
                            persona.getUsername(),
                            persona.getId(),
                            persona.getRol()
                    );

                    return ResponseEntity.ok(
                            Map.ofEntries(
                                    Map.entry("token", token),
                                    Map.entry("username", persona.getUsername()),
                                    Map.entry("rol", persona.getRol())
                            )
                    );
                })
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body(Map.of("error", "Credenciales inválidas"))))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Error interno en autenticación"))));
    }
}
