package com.ecole221.banque_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Schema(description = "Représentation d'un client")
public class ClientDto {
    @Schema(description = "Identifiant du client", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Numéro de pièce unique", example = "CNI-10001")
    @NotBlank(message = "Le numéro de pièce est obligatoire")
    @Size(min = 5, max = 50, message = "Le numéro de pièce doit contenir entre 5 et 50 caractères")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Le numéro de pièce ne doit contenir que des lettres, chiffres et tirets")
    private String numPiece;

    @Schema(description = "Prénom du client", example = "Awa")
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Le prénom contient des caractères non autorisés")
    private String prenom;

    @Schema(description = "Nom du client", example = "Diallo")
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Le nom contient des caractères non autorisés")
    private String nom;

    @Schema(description = "Adresse du client", example = "Dakar")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String adresse;

    @Schema(description = "Date de naissance", example = "1998-05-12")
    @NotNull(message = "La date de naissance est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    @Schema(description = "Téléphone du client", example = "+221771234567")
    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(min = 8, max = 20, message = "Le téléphone doit contenir entre 8 et 20 caractères")
    @Pattern(regexp = "^[0-9+\\s-]{8,20}$", message = "Numéro de téléphone invalide")
    private String telephone;
}
