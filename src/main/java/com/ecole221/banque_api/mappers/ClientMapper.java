package com.ecole221.banque_api.mappers;

import com.ecole221.banque_api.dto.ClientDto;
import com.ecole221.banque_api.models.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDto toDto(Client client) {
        if (client == null) return null;
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNumPiece(client.getNumPiece());
        dto.setPrenom(client.getPrenom());
        dto.setNom(client.getNom());
        dto.setAdresse(client.getAdresse());
        dto.setDateNaissance(client.getDateNaissance());
        dto.setTelephone(client.getTelephone());
        return dto;
    }

    public Client toEntity(ClientDto dto) {
        if (dto == null) return null;
        Client client = new Client();
        client.setNumPiece(dto.getNumPiece());
        client.setPrenom(dto.getPrenom());
        client.setNom(dto.getNom());
        client.setAdresse(dto.getAdresse());
        client.setDateNaissance(dto.getDateNaissance());
        client.setTelephone(dto.getTelephone());
        return client;
    }
}
