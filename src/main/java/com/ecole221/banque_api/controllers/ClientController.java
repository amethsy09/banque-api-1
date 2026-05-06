package com.ecole221.banque_api.controllers;

import com.ecole221.banque_api.dto.ClientDto;
import com.ecole221.banque_api.helpers.ClientHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Validated
@Tag(name = "Clients", description = "Création et consultation des clients")
public class ClientController {

    private final ClientHelper clientHelper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Créer un client",
            description = """
                    Crée un nouveau client dans le système.
                    
                    Autorisation :
                    - rôle requis : `ADMIN`
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou client déjà existant")
    })
    public ResponseEntity<ClientDto> creerClient(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informations du client à créer",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClientDto.class),
                            examples = @ExampleObject(
                                    name = "Nouveau client",
                                    value = """
                                            {
                                              "numPiece": "CNI-10001",
                                              "prenom": "Awa",
                                              "nom": "Diallo",
                                              "adresse": "Dakar",
                                              "dateNaissance": "1998-05-12",
                                              "telephone": "+221771234567"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody ClientDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientHelper.creerClient(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Lister tous les clients",
            description = """
                    Retourne tous les clients enregistrés.
                    
                    Autorisation :
                    - rôle requis : `ADMIN`
                    """
    )
    @ApiResponse(responseCode = "200", description = "Liste des clients")
    public ResponseEntity<List<ClientDto>> listerTous() {
        return ResponseEntity.ok(clientHelper.listerTous());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @authorizationHelper.canAccessClient(#id)")
    @Operation(
            summary = "Afficher un client",
            description = """
                    Retourne les informations d'un client par son identifiant.
                    
                    Autorisation :
                    - `ADMIN`
                    - ou `USER` propriétaire des données
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<ClientDto> trouverParId(
            @Parameter(description = "ID numérique du client", example = "1")
            @Positive(message = "L'id du client doit être positif")
            @PathVariable Integer id) {
        return ResponseEntity.ok(clientHelper.trouverParId(id));
    }
}
