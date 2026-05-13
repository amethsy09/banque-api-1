package com.ecole221.banque_api.helpers;

import com.ecole221.banque_api.dto.ClientDto;
import com.ecole221.banque_api.exceptions.ResourceNotFoundException;
import com.ecole221.banque_api.exceptions.TransactionException;
import com.ecole221.banque_api.mappers.ClientMapper;
import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientHelper {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientDto creerClient(ClientDto dto) {
        if (clientService.existsByNumPiece(dto.getNumPiece())) {
            throw new TransactionException("Un client avec ce numero de piece existe deja.");
        }
        if (clientService.existsByTelephone(dto.getTelephone())) {
            throw new TransactionException("Un client avec ce numero de telephone existe deja.");
        }

        Client client = clientMapper.toEntity(dto);
        return clientMapper.toDto(clientService.save(client));
    }

    public List<ClientDto> listerTous() {
        return clientService.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    public ClientDto trouverParId(Integer id) {
        Client client = getOrThrow(clientService.findById(id), "Client introuvable avec l'id: " + id);
        return clientMapper.toDto(client);
    }

    private <T> T getOrThrow(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(message));
    }
}
