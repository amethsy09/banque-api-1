package com.ecole221.banque_api.helpers;

import com.ecole221.banque_api.dto.CompteCreateDto;
import com.ecole221.banque_api.dto.CompteDto;
import com.ecole221.banque_api.dto.TransactionDto;
import com.ecole221.banque_api.dto.TransactionRequestDto;
import com.ecole221.banque_api.exceptions.ResourceNotFoundException;
import com.ecole221.banque_api.exceptions.TransactionException;
import com.ecole221.banque_api.mappers.CompteMapper;
import com.ecole221.banque_api.mappers.TransactionMapper;
import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.models.Compte;
import com.ecole221.banque_api.models.Transaction;
import com.ecole221.banque_api.models.Type;
import com.ecole221.banque_api.services.ClientService;
import com.ecole221.banque_api.services.CompteService;
import com.ecole221.banque_api.services.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompteHelper {

    private static final long DEPOT_MINIMUM = 10_000L;
    private static final long SOLDE_MINIMUM = -50_000L;
    private static final long SOLDE_MAXIMUM = 50_000L;

    private final CompteService compteService;
    private final ClientService clientService;
    private final TypeService typeService;
    private final CompteMapper compteMapper;
    private final TransactionMapper transactionMapper;
    private final NumeroCompteHelper numeroCompteHelper;

    public CompteDto creerCompte(CompteCreateDto dto) {
        Client client = clientService.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id: " + dto.getClientId()));

        Compte compte = new Compte();
        compte.setSolde(dto.getSoldeInitial());
        compte.setNumero(genererNumeroUnique());
        client.addCompte(compte);

        return compteMapper.toDto(compteService.save(compte));
    }

    public List<CompteDto> listerComptesDuClient(Integer clientId) {
        if (!clientService.existsById(clientId)) {
            throw new ResourceNotFoundException("Client introuvable avec l'id: " + clientId);
        }

        return compteService.findByClientId(clientId).stream()
                .map(compteMapper::toDto)
                .toList();
    }

    public CompteDto rechercherParNumero(String numero) {
        return compteMapper.toDto(compteService.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable avec le numero: " + numero)));
    }

    public CompteDto afficherInfos(Integer id) {
        return compteMapper.toDto(compteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable avec l'id: " + id)));
    }

    public TransactionDto effectuerTransaction(TransactionRequestDto request) {
        Compte compte = compteService.findByNumero(request.getCompteNumero())
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable: " + request.getCompteNumero()));

        String typeLibelle = request.getType().toUpperCase().trim();
        long montant = request.getMontant();

        switch (typeLibelle) {
            case "DEPOT" -> effectuerDepot(compte, montant);
            case "RETRAIT" -> effectuerRetrait(compte, montant);
            default -> throw new TransactionException("Type de transaction invalide. Utiliser DEPOT ou RETRAIT.");
        }

        Type type = typeService.findByLibelleIgnoreCase(typeLibelle)
                .orElseGet(() -> {
                    Type nouveauType = new Type();
                    nouveauType.setLibelle(typeLibelle);
                    return nouveauType;
                });

        Transaction transaction = new Transaction();
        transaction.setMontant(montant);
        transaction.setType(type);
        compte.addTransaction(transaction);

        compteService.save(compte);
        return transactionMapper.toDto(transaction);
    }

    private void effectuerDepot(Compte compte, long montant) {
        if (montant < DEPOT_MINIMUM) {
            throw new TransactionException(
                    "Le depot minimum est de " + DEPOT_MINIMUM + " FCFA. Montant fourni: " + montant);
        }

        compte.setSolde(compte.getSolde() + montant);
    }

    private void effectuerRetrait(Compte compte, long montant) {
        long soldeActuel = compte.getSolde();
        long soldeApresRetrait = soldeActuel - montant;

        if (soldeApresRetrait < SOLDE_MINIMUM) {
            throw new TransactionException(
                    "Retrait refuse. Le solde ne peut pas etre inferieur a " + SOLDE_MINIMUM + " FCFA. "
                            + "Solde actuel: " + soldeActuel + " FCFA.");
        }

        if (soldeActuel > SOLDE_MAXIMUM) {
            long retraitMax = soldeActuel - SOLDE_MAXIMUM;
            throw new TransactionException(
                    "Votre solde depasse " + SOLDE_MAXIMUM + " FCFA (solde actuel: " + soldeActuel + " FCFA). "
                            + "Vous ne pouvez retirer que " + retraitMax + " FCFA maximum.");
        }

        compte.setSolde(soldeApresRetrait);
    }

    private String genererNumeroUnique() {
        String numero;
        do {
            numero = numeroCompteHelper.generer();
        } while (compteService.existsByNumero(numero));
        return numero;
    }
}
