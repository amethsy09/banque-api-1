package com.ecole221.banque_api.services;

import com.ecole221.banque_api.models.Categorie;
import com.ecole221.banque_api.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TypeService {

    private final TypeRepository typeRepository;

    @Transactional(readOnly = true)
    public Optional<Categorie> findByLibelleIgnoreCase(String libelle) {
        return typeRepository.findByLibelleIgnoreCase(libelle);
    }

    public Categorie save(Categorie type) {
        return typeRepository.save(type);
    }
}
