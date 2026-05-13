package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Categorie, Integer> {
    Optional<Categorie> findByLibelleIgnoreCase(String libelle);
}
