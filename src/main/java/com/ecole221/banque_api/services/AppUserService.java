package com.ecole221.banque_api.services;

import com.ecole221.banque_api.models.AppUser;
import com.ecole221.banque_api.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    public boolean existsByClientId(Integer clientId) {
        return appUserRepository.existsByClientId(clientId);
    }

    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
