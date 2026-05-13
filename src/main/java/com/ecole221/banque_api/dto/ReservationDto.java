package com.ecole221.banque_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {
    private Integer id;
    private String numero;
    private long solde;
    private LocalDate dateCreation;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Integer clientId;

    private String clientNomComplet;
}
