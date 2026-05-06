package com.ecole221.banque_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Données nécessaires à l'exécution d'une transaction")
public class TransactionRequestDto {

    @Schema(description = "Numéro du compte cible", example = "BQ-20250101-A1B2C3D4")
    @NotBlank(message = "Le numéro de compte est obligatoire")
    @Pattern(
            regexp = "^BQ-\\d{8}-[A-Z0-9]{8}$",
            message = "Le numéro de compte doit respecter le format BQ-YYYYMMDD-XXXXXXXX"
    )
    private String compteNumero;

    @Schema(description = "Montant de l'opération", example = "15000")
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Long montant;

    @Schema(description = "Type d'opération", allowableValues = {"DEPOT", "RETRAIT"}, example = "DEPOT")
    @NotBlank(message = "Le type de transaction est obligatoire")
    @Pattern(regexp = "^(DEPOT|RETRAIT)$", message = "Le type doit être DEPOT ou RETRAIT")
    private String type;
}
