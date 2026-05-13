package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByNumPiece(String numPiece);
    Optional<Client> findByTelephone(String telephone);
    boolean existsByNumPiece(String numPiece);
    boolean existsByTelephone(String telephone);

    @EntityGraph(attributePaths = "appUser")
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.appUser WHERE c.id = :id")
    Optional<Client> findByIdWithAppUser(@Param("id") Integer id);

    @EntityGraph(attributePaths = "comptes")
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.comptes WHERE c.id = :id")
    Optional<Client> findByIdWithComptes(@Param("id") Integer id);
}
