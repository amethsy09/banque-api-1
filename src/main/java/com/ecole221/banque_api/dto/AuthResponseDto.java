package com.ecole221.banque_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String type;
    private String username;
    private String role;
}
