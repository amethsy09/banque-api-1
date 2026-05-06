package com.ecole221.banque_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Données nécessaires à la création d'un compte")
public class CompteCreateDto {

    @Schema(description = "ID du client propriétaire du compte", example = "1")
    @NotNull(message = "Le champ clientId est obligatoire.")
    @Positive(message = "Le champ clientId doit être positif.")
    private Integer clientId;

    @Schema(description = "Solde initial du compte", example = "25000")
    @NotNull(message = "Le champ soldeInitial est obligatoire.")
    @Min(value = 0, message = "Le champ soldeInitial doit être supérieur ou égal à 0.")
    private Long soldeInitial = 0L;
}
