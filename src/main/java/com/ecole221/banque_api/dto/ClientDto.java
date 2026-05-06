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

    @Schema(description = "Numéro de pièce unique composé de 13 chiffres", example = "1234567890123")
    @NotBlank(message = "Le champ numPiece est obligatoire.")
    @Pattern(regexp = "^\\d{13}$", message = "Le champ numPiece doit contenir exactement 13 chiffres.")
    private String numPiece;

    @Schema(description = "Prénom du client", example = "Awa")
    @NotBlank(message = "Le champ prenom est obligatoire.")
    @Size(min = 2, max = 100, message = "Le champ prenom doit contenir entre 2 et 100 caractères.")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Le champ prenom contient des caractères non autorisés.")
    private String prenom;

    @Schema(description = "Nom du client", example = "Diallo")
    @NotBlank(message = "Le champ nom est obligatoire.")
    @Size(min = 2, max = 100, message = "Le champ nom doit contenir entre 2 et 100 caractères.")
    @Pattern(regexp = "^[\\p{L} '-]+$", message = "Le champ nom contient des caractères non autorisés.")
    private String nom;

    @Schema(description = "Adresse du client", example = "Dakar")
    @Size(max = 255, message = "Le champ adresse ne doit pas dépasser 255 caractères.")
    private String adresse;

    @Schema(description = "Date de naissance", example = "1998-05-12")
    @NotNull(message = "Le champ dateNaissance est obligatoire.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Le champ dateNaissance doit être une date passée.")
    private LocalDate dateNaissance;

    @Schema(
            description = "Téléphone du client au format sénégalais. Exemples acceptés : 771234567, 77 123 45 67, +221 77 123 45 67",
            example = "+221 77 123 45 67"
    )
    @NotBlank(message = "Le champ telephone est obligatoire.")
    @Pattern(
            regexp = "^(?:((70|75|76|77|78)\\d{7})|((70|75|76|77|78) \\d{3} \\d{2} \\d{2})|(\\+221 (70|75|76|77|78) \\d{3} \\d{2} \\d{2}))$",
            message = "Le champ telephone doit être au format sénégalais valide : 7XXXXXXXX, 7X XXX XX XX ou +221 7X XXX XX XX."
    )
    private String telephone;
}
