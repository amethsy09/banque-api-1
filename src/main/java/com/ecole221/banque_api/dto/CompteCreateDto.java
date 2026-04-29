package com.ecole221.banque_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CompteCreateDto {

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long clientId;

    @Min(value = 0, message = "Le solde initial ne peut pas être négatif")
    private long soldeInitial = 0;
}
