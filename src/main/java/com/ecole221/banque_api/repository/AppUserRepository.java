package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByClientId(Integer clientId);
}
