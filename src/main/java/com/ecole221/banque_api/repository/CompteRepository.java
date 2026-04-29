package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.models.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByNumero(String numero);
    List<Compte> findByClient(Client client);
    List<Compte> findByClientId(Long clientId);
    boolean existsByNumero(String numero);
}
