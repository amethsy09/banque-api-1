package com.ecole221.banque_api.controllers;

import com.ecole221.banque_api.dto.ClientDto;
import com.ecole221.banque_api.services.ClientService;
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
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients de la banque")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Créer un client", description = "Enregistre un nouveau client dans le système")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou client déjà existant")
    })
    public ResponseEntity<ClientDto> creerClient(@Valid @RequestBody ClientDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.creerClient(dto));
    }

    @GetMapping
    @Operation(summary = "Lister tous les clients", description = "Retourne la liste de tous les clients enregistrés")
    @ApiResponse(responseCode = "200", description = "Liste des clients")
    public ResponseEntity<List<ClientDto>> listerTous() {
        return ResponseEntity.ok(clientService.listerTous());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Afficher un client", description = "Retourne les informations d'un client par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    public ResponseEntity<ClientDto> trouverParId(
            @Parameter(description = "ID du client") @PathVariable Long id) {
        return ResponseEntity.ok(clientService.trouverParId(id));
    }
}