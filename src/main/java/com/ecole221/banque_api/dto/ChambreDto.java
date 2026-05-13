package com.ecole221.banque_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChambreDto {
    private Integer id;
    private long montant;
    private LocalDate date;
    private String typeLibelle;
    private String compteNumero;
}
