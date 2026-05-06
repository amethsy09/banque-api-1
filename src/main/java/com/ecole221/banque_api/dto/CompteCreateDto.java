package com.ecole221.banque_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Données nécessaires à la création d'un compte")
public class CompteCreateDto {

    @Schema(description = "ID du client propriétaire du compte", example = "1")
    @NotNull(message = "L'identifiant du client est obligatoire")
    @Positive(message = "L'identifiant du client doit être positif")
    private Integer clientId;

    @Schema(description = "Solde initial du compte", example = "25000")
    @NotNull(message = "Le solde initial est obligatoire")
    @Min(value = 0, message = "Le solde initial ne peut pas être négatif")
    private Long soldeInitial = 0L;
}
