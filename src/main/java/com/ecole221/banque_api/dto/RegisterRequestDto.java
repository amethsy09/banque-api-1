package com.ecole221.banque_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Données nécessaires à la création d'un utilisateur applicatif")
public class RegisterRequestDto {

    @Schema(description = "Nom d'utilisateur unique", example = "user1")
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Le nom d'utilisateur contient des caractères non autorisés")
    private String username;

    @Schema(description = "Mot de passe", example = "123456")
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, max = 100, message = "Le mot de passe doit contenir entre 6 et 100 caractères")
    private String password;

    @Schema(description = "Rôle de l'utilisateur", allowableValues = {"ADMIN", "USER"}, example = "USER")
    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(regexp = "^(ADMIN|USER|ROLE_ADMIN|ROLE_USER)$", message = "Le rôle doit être ADMIN ou USER")
    private String role;

    @Schema(description = "ID du client à rattacher pour un rôle USER", example = "1", nullable = true)
    @Positive(message = "Le clientId doit être positif")
    private Integer clientId;

    @AssertTrue(message = "Un utilisateur USER doit fournir clientId, et un ADMIN ne doit pas fournir clientId")
    public boolean isClientIdCoherentWithRole() {
        if (role == null || role.isBlank()) {
            return true;
        }

        String normalizedRole = role.trim().toUpperCase();
        if ("USER".equals(normalizedRole) || "ROLE_USER".equals(normalizedRole)) {
            return clientId != null;
        }
        if ("ADMIN".equals(normalizedRole) || "ROLE_ADMIN".equals(normalizedRole)) {
            return clientId == null;
        }
        return true;
    }
}
