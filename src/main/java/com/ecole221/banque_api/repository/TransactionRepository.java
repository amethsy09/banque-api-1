package com.ecole221.banque_api.repository;

import com.ecole221.banque_api.models.Compte;
import com.ecole221.banque_api.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByCompte(Compte compte);
    List<Transaction> findByCompteNumero(String compteNumero);
}
