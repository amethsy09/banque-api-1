package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNumPiece(String numPiece);
    Optional<Client> findByTelephone(String telephone);
    boolean existsByNumPiece(String numPiece);
    boolean existsByTelephone(String telephone);
}
