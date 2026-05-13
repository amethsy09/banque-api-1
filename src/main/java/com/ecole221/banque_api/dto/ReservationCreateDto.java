package com.ecole221.banque_api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Données nécessaires à la reservation d'une chambre d'hotel")
public class ReservationCreateDto {

    @Schema(description = "ID du client propriétaire de la reservation", example = "1")
    @NotNull(message = "Le champ clientId est obligatoire.")
    @Positive(message = "Le champ clientId doit être positif.")
    private Integer clientId;

  
}
