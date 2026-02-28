package com.example.ms_personas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonaLoginResponse {

    private Long id;
    private String username;
    private String rol;
}
