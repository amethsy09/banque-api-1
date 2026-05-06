package com.ecole221.banque_api.services;

import com.ecole221.banque_api.models.Compte;
import com.ecole221.banque_api.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompteService {

    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public boolean existsByNumero(String numero) {
        return compteRepository.existsByNumero(numero);
    }

    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

    @Transactional(readOnly = true)
    public List<Compte> findByClientId(Integer clientId) {
        return compteRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public Optional<Compte> findByNumero(String numero) {
        return compteRepository.findByNumero(numero);
    }

    @Transactional(readOnly = true)
    public Optional<Compte> findById(Integer id) {
        return compteRepository.findById(id);
    }
}
