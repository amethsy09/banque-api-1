package com.ecole221.banque_api.controllers;

import com.ecole221.banque_api.dto.CompteCreateDto;
import com.ecole221.banque_api.dto.CompteDto;
import com.ecole221.banque_api.dto.TransactionDto;
import com.ecole221.banque_api.dto.TransactionRequestDto;
import com.ecole221.banque_api.services.CompteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Tag(name = "Comptes", description = "Gestion des comptes bancaires")
public class CompteController {

    private final CompteService compteService;

    @PostMapping
    @Operation(summary = "Créer un compte", description = "Crée un nouveau compte bancaire pour un client existant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<CompteDto> creerCompte(@Valid @RequestBody CompteCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compteService.creerCompte(dto));
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Lister les comptes d'un client", description = "Retourne tous les comptes associés à un client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des comptes"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<List<CompteDto>> listerComptesDuClient(
            @Parameter(description = "ID du client") @PathVariable Long clientId) {
        return ResponseEntity.ok(compteService.listerComptesDuClient(clientId));
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Rechercher un compte par numéro", description = "Recherche un compte par son numéro unique")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compte trouvé"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<CompteDto> rechercherParNumero(
            @Parameter(description = "Numéro du compte (ex: BQ-20250101-A1B2C3D4)") @PathVariable String numero) {
        return ResponseEntity.ok(compteService.rechercherParNumero(numero));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher les infos d'un compte", description = "Affiche les informations détaillées d'un compte par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Infos du compte"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<CompteDto> afficherInfos(
            @Parameter(description = "ID du compte") @PathVariable Long id) {
        return ResponseEntity.ok(compteService.afficherInfos(id));
    }

    @PostMapping("/transaction")
    @Operation(
            summary = "Effectuer une transaction",
            description = """
            Effectue un dépôt ou un retrait sur un compte.
            
            **Contraintes métier :**
            - Dépôt minimum : 10 000 FCFA
            - Le solde ne peut pas descendre en dessous de -50 000 FCFA
            - Si le solde dépasse 50 000 FCFA, le retrait est limité à la différence avec 50 000 FCFA
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction effectuée avec succès"),
            @ApiResponse(responseCode = "400", description = "Transaction refusée (contrainte métier ou données invalides)"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<TransactionDto> effectuerTransaction(@Valid @RequestBody TransactionRequestDto request) {
        return ResponseEntity.ok(compteService.effectuerTransaction(request));
    }
}
