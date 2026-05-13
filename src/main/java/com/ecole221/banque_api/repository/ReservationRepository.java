package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.services.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByNumero(String numero);
    List<Reservation> findByClient(Client client);
    List<Reservation> findByClientId(Integer clientId);
    boolean existsByNumero(String numero);
}
