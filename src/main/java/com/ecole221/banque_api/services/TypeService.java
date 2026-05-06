package com.ecole221.banque_api.services;

import com.ecole221.banque_api.models.Type;
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
    public Optional<Type> findByLibelleIgnoreCase(String libelle) {
        return typeRepository.findByLibelleIgnoreCase(libelle);
    }
}
