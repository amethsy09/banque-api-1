package com.ecole221.banque_api.controllers;

import com.ecole221.banque_api.dto.AuthRequestDto;
import com.ecole221.banque_api.dto.AuthResponseDto;
import com.ecole221.banque_api.dto.RegisterRequestDto;
import com.ecole221.banque_api.helpers.AuthHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentification", description = "Inscription, connexion et récupération de token JWT")
public class AuthController {

    private final AuthHelper authHelper;

    @PostMapping("/register")
    @Operation(
            summary = "Créer un utilisateur",
            description = """
                    Crée un utilisateur applicatif et retourne immédiatement un token JWT.
                    
                    Règles :
                    - `ADMIN` : ne nécessite pas de `clientId`
                    - `USER` : nécessite un `clientId` existant
                    - un client ne peut être rattaché qu'à un seul utilisateur
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides, rôle invalide ou utilisateur déjà existant")
    })
    public ResponseEntity<AuthResponseDto> register(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données d'inscription",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Creation ADMIN",
                                            value = """
                                                    {
                                                      "username": "admin1",
                                                      "password": "123456",
                                                      "role": "ADMIN"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Creation USER",
                                            value = """
                                                    {
                                                      "username": "user1",
                                                      "password": "123456",
                                                      "role": "USER",
                                                      "clientId": 1
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authHelper.register(request));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Se connecter",
            description = """
                    Authentifie un utilisateur avec son nom d'utilisateur et son mot de passe.
                    Retourne un token JWT à utiliser dans le bouton `Authorize` de Swagger.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    public ResponseEntity<AuthResponseDto> login(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Identifiants de connexion",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Connexion",
                                    value = """
                                            {
                                              "username": "admin1",
                                              "password": "123456"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(authHelper.login(request));
    }
}
