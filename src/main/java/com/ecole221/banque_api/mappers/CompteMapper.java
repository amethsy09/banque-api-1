package com.ecole221.banque_api.mappers;

import com.ecole221.banque_api.dto.CompteDto;
import com.ecole221.banque_api.models.Compte;
import org.springframework.stereotype.Component;

@Component
public class CompteMapper {

    public CompteDto toDto(Compte compte) {
        if (compte == null) return null;
        CompteDto dto = new CompteDto();
        dto.setId(compte.getId());
        dto.setNumero(compte.getNumero());
        dto.setSolde(compte.getSolde());
        dto.setDateCreation(compte.getDateCreation());
        if (compte.getClient() != null) {
            dto.setClientId(compte.getClient().getId());
            dto.setClientNomComplet(compte.getClient().getPrenom() + " " + compte.getClient().getNom());
        }
        return dto;
    }
}
