package com.ecole221.banque_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TransactionRequestDto {

    @NotNull(message = "Le numéro de compte est obligatoire")
    private String compteNumero;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private long montant;

    @NotBlank(message = "Le type de transaction est obligatoire")
    private String type;
}
