package com.ecole221.banque_api.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI banqueOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Saisir uniquement le token JWT sans ajouter le prefixe Bearer.")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .addServersItem(new Server().url("http://localhost:8080").description("Serveur local"))
                .info(new Info()
                        .title("Banque API")
                        .description("""
                                API REST de gestion bancaire avec authentification JWT, gestion des clients,
                                gestion des comptes et opérations de transaction.
                                
                                Guide rapide :
                                1. Créer un utilisateur via `/api/auth/register`
                                2. Se connecter via `/api/auth/login`
                                3. Copier le token JWT reçu
                                4. Cliquer sur le bouton `Authorize` dans Swagger
                                5. Coller le token pour accéder aux endpoints protégés
                                
                                Roles applicatifs :
                                - `ADMIN` : peut créer clients, comptes et transactions, et consulter toutes les données
                                - `USER` : peut consulter uniquement ses propres données selon les règles d'autorisation
                                
                                Contraintes métier :
                                - Dépôt minimum : 10 000 FCFA
                                - Solde minimum autorisé : -50 000 FCFA
                                - Si le solde actuel dépasse 50 000 FCFA, le retrait est restreint par la règle métier implémentée
                                
                                Format des identifiants :
                                - les IDs sont de type entier simple (`Integer`)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("L3GL - Équipe Banque")
                                .email("ahmadShema@l3gl.sn"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
