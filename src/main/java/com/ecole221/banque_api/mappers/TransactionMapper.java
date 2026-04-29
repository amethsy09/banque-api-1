package com.ecole221.banque_api.mappers;

import com.ecole221.banque_api.dto.TransactionDto;
import com.ecole221.banque_api.models.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) return null;
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setMontant(transaction.getMontant());
        dto.setDate(transaction.getDate());
        if (transaction.getType() != null) {
            dto.setTypeLibelle(transaction.getType().getLibelle());
        }
        if (transaction.getCompte() != null) {
            dto.setCompteNumero(transaction.getCompte().getNumero());
        }
        return dto;
    }
}
