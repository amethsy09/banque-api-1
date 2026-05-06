package com.ecole221.banque_api.controllers;

import com.ecole221.banque_api.dto.CompteCreateDto;
import com.ecole221.banque_api.dto.CompteDto;
import com.ecole221.banque_api.dto.TransactionDto;
import com.ecole221.banque_api.dto.TransactionRequestDto;
import com.ecole221.banque_api.helpers.CompteHelper;
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
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Comptes", description = "Création, consultation et transactions sur les comptes")
public class CompteController {

    private final CompteHelper compteHelper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Créer un compte",
            description = """
                    Crée un nouveau compte bancaire pour un client existant.
                    
                    Autorisation :
                    - rôle requis : `ADMIN`
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<CompteDto> creerCompte(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informations nécessaires à la création d'un compte",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CompteCreateDto.class),
                            examples = @ExampleObject(
                                    name = "Nouveau compte",
                                    value = """
                                            {
                                              "clientId": 1,
                                              "soldeInitial": 25000
                                            }
                                            """
                            )
                    )
            )
            @RequestBody CompteCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compteHelper.creerCompte(dto));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or @authorizationHelper.canAccessClient(#clientId)")
    @Operation(
            summary = "Lister les comptes d'un client",
            description = """
                    Retourne tous les comptes d'un client.
                    
                    Autorisation :
                    - `ADMIN`
                    - ou `USER` propriétaire
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des comptes"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<List<CompteDto>> listerComptesDuClient(
            @Parameter(description = "ID numérique du client", example = "1")
            @Positive(message = "L'id du client doit être positif")
            @PathVariable Integer clientId) {
        return ResponseEntity.ok(compteHelper.listerComptesDuClient(clientId));
    }

    @GetMapping("/numero/{numero}")
    @PreAuthorize("hasRole('ADMIN') or @authorizationHelper.canAccessCompteByNumero(#numero)")
    @Operation(
            summary = "Rechercher un compte par numéro",
            description = """
                    Recherche un compte via son numéro unique.
                    
                    Autorisation :
                    - `ADMIN`
                    - ou `USER` propriétaire
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compte trouvé"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<CompteDto> rechercherParNumero(
            @Parameter(description = "Numéro du compte", example = "BQ-20250101-A1B2C3D4") @PathVariable String numero) {
        return ResponseEntity.ok(compteHelper.rechercherParNumero(numero));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @authorizationHelper.canAccessCompteById(#id)")
    @Operation(
            summary = "Afficher les informations d'un compte",
            description = """
                    Retourne le détail d'un compte via son identifiant.
                    
                    Autorisation :
                    - `ADMIN`
                    - ou `USER` propriétaire
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Infos du compte"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<CompteDto> afficherInfos(
            @Parameter(description = "ID numérique du compte", example = "1")
            @Positive(message = "L'id du compte doit être positif")
            @PathVariable Integer id) {
        return ResponseEntity.ok(compteHelper.afficherInfos(id));
    }

    @PostMapping("/transaction")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Effectuer une transaction",
            description = """
                    Effectue un dépôt ou un retrait sur un compte.
                    
                    Autorisation :
                    - rôle requis : `ADMIN`
                    
                    Contraintes métier :
                    - dépôt minimum : 10 000 FCFA
                    - le solde ne peut pas descendre en dessous de -50 000 FCFA
                    - la règle métier de retrait spécial au-dessus de 50 000 FCFA s'applique
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction effectuée avec succès"),
            @ApiResponse(responseCode = "400", description = "Transaction refusée (contrainte métier ou données invalides)"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<TransactionDto> effectuerTransaction(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Demande de dépôt ou de retrait",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TransactionRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Depot",
                                            value = """
                                                    {
                                                      "compteNumero": "BQ-20250101-A1B2C3D4",
                                                      "montant": 15000,
                                                      "type": "DEPOT"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Retrait",
                                            value = """
                                                    {
                                                      "compteNumero": "BQ-20250101-A1B2C3D4",
                                                      "montant": 10000,
                                                      "type": "RETRAIT"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody TransactionRequestDto request) {
        return ResponseEntity.ok(compteHelper.effectuerTransaction(request));
    }
}
