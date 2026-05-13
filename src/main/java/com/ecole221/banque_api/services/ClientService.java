package com.ecole221.banque_api.services;

import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public boolean existsByNumPiece(String numPiece) {
        return clientRepository.existsByNumPiece(numPiece);
    }

    public boolean existsByTelephone(String telephone) {
        return clientRepository.existsByTelephone(telephone);
    }

    public boolean existsById(Integer id) {
        return clientRepository.existsById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByIdWithAppUser(Integer id) {
        return clientRepository.findByIdWithAppUser(id);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByIdWithComptes(Integer id) {
        return clientRepository.findByIdWithComptes(id);
    }
}
