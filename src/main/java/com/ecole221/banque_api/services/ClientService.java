package com.ecole221.banque_api.services;

import com.ecole221.banque_api.dto.ClientDto;
import com.ecole221.banque_api.exceptions.ResourceNotFoundException;
import com.ecole221.banque_api.exceptions.TransactionException;
import com.ecole221.banque_api.mappers.ClientMapper;
import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientDto creerClient(ClientDto dto) {
        if (clientRepository.existsByNumPiece(dto.getNumPiece())) {
            throw new TransactionException("Un client avec ce numéro de pièce existe déjà.");
        }
        if (clientRepository.existsByTelephone(dto.getTelephone())) {
            throw new TransactionException("Un client avec ce numéro de téléphone existe déjà.");
        }
        Client client = clientMapper.toEntity(dto);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Transactional(readOnly = true)
    public List<ClientDto> listerTous() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDto trouverParId(Long id) {
        return clientMapper.toDto(
                clientRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id: " + id))
        );
    }
}
