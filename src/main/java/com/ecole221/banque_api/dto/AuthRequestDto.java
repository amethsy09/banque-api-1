package com.ecole221.banque_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Données nécessaires à la connexion d'un utilisateur")
public class AuthRequestDto {

    @Schema(description = "Nom d'utilisateur", example = "admin1")
    @NotBlank(message = "Le champ username est obligatoire.")
    @Size(min = 3, max = 50, message = "Le champ username doit contenir entre 3 et 50 caractères.")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Le champ username contient des caractères non autorisés.")
    private String username;

    @Schema(description = "Mot de passe", example = "123456")
    @NotBlank(message = "Le champ password est obligatoire.")
    @Size(min = 6, max = 100, message = "Le champ password doit contenir entre 6 et 100 caractères.")
    private String password;
}
