package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Reservation;
import com.ecole221.banque_api.models.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Integer> {
    List<Chambre> findByCompte(Reservation compte);
    List<Chambre> findByCompteNumero(String compteNumero);
}
