package com.ecole221.banque_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompteDto {
    private Long id;
    private String numero;
    private long solde;
    private LocalDate dateCreation;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long clientId;

    private String clientNomComplet;
}
