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
    @NotBlank(message = "Le champ username est obligatoire.")
    @Size(min = 3, max = 50, message = "Le champ username doit contenir entre 3 et 50 caractères.")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Le champ username contient des caractères non autorisés.")
    private String username;

    @Schema(description = "Mot de passe", example = "123456")
    @NotBlank(message = "Le champ password est obligatoire.")
    @Size(min = 6, max = 100, message = "Le champ password doit contenir entre 6 et 100 caractères.")
    private String password;

    @Schema(description = "Rôle de l'utilisateur", allowableValues = {"ADMIN", "USER"}, example = "USER")
    @NotBlank(message = "Le champ role est obligatoire.")
    @Pattern(regexp = "^(ADMIN|USER|ROLE_ADMIN|ROLE_USER)$", message = "Le champ role doit être égal à ADMIN ou USER.")
    private String role;

    @Schema(description = "ID du client à rattacher pour un rôle USER", example = "1", nullable = true)
    @Positive(message = "Le champ clientId doit être positif.")
    private Integer clientId;

    @AssertTrue(message = "Le champ clientId est obligatoire pour USER et doit être absent pour ADMIN.")
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
