package com.ecole221.banque_api.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI banqueOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banque API")
                        .description("""
                                API REST de gestion bancaire permettant :
                                - La création et consultation de comptes
                                - La gestion des clients
                                - Les opérations de dépôt et retrait avec contraintes métier
                                
                                **Contraintes métier :**
                                - Dépôt minimum : 10 000 FCFA
                                - Solde minimum autorisé : -50 000 FCFA
                                - Si solde > 50 000 FCFA : retrait limité à (solde - 50 000) FCFA
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("L3GL - Équipe Banque")
                                .email("contact@l3gl.sn"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
