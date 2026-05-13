package com.ecole221.banque_api.helpers;

import com.ecole221.banque_api.dto.ReservationCreateDto;
import com.ecole221.banque_api.dto.ReservationDto;
import com.ecole221.banque_api.dto.ChambreDto;
import com.ecole221.banque_api.dto.TransactionRequestDto;
import com.ecole221.banque_api.exceptions.ResourceNotFoundException;
import com.ecole221.banque_api.exceptions.TransactionException;
import com.ecole221.banque_api.mappers.ReservationMapper;
import com.ecole221.banque_api.mappers.ChambreMapper;
import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.models.Reservation;
import com.ecole221.banque_api.models.Chambre;
import com.ecole221.banque_api.models.Categorie;
import com.ecole221.banque_api.services.ClientService;
import com.ecole221.banque_api.services.Reservation;
import com.ecole221.banque_api.services.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompteHelper {

    private static final long DEPOT_MINIMUM = 10_000L;
    private static final long SOLDE_MINIMUM = -50_000L;
    private static final long SOLDE_MAXIMUM = 50_000L;

    private final Reservation compteService;
    private final ClientService clientService;
    private final TypeService typeService;
    private final ReservationMapper compteMapper;
    private final ChambreMapper transactionMapper;
    private final NumeroCompteHelper numeroCompteHelper;

    public ReservationDto creerCompte(ReservationCreateDto dto) {
        Client client = getOrThrow(clientService.findById(dto.getClientId()), "Client introuvable avec l'id: " + dto.getClientId());

        Reservation compte = new Reservation();
        compte.setSolde(dto.getSoldeInitial());
        compte.setNumero(genererNumeroUnique());
        client.addCompte(compte);

        return compteMapper.toDto(compteService.save(compte));
    }

    public List<ReservationDto> listerComptesDuClient(Integer clientId) {
        Client client = getOrThrow(clientService.findByIdWithComptes(clientId), "Client introuvable avec l'id: " + clientId);

        return client.getComptes().stream()
                .map(compteMapper::toDto)
                .toList();
    }

    public ReservationDto rechercherParNumero(String numero) {
        Reservation compte = getOrThrow(compteService.findByNumero(numero), "Compte introuvable avec le numero: " + numero);
        return compteMapper.toDto(compte);
    }

    public ReservationDto afficherInfos(Integer id) {
        Reservation compte = getOrThrow(compteService.findById(id), "Compte introuvable avec l'id: " + id);
        return compteMapper.toDto(compte);
    }

    public ChambreDto effectuerTransaction(TransactionRequestDto request) {
        Reservation compte = getOrThrow(compteService.findByNumero(request.getCompteNumero()), "Compte introuvable: " + request.getCompteNumero());

        String typeLibelle = request.getType().toUpperCase().trim();
        long montant = request.getMontant();

        switch (typeLibelle) {
            case "DEPOT" -> effectuerDepot(compte, montant);
            case "RETRAIT" -> effectuerRetrait(compte, montant);
            default -> throw new TransactionException("Type de transaction invalide. Utiliser DEPOT ou RETRAIT.");
        }

        Categorie type = typeService.findByLibelleIgnoreCase(typeLibelle)
                .orElseGet(() -> {
                    Categorie nouveauType = new Categorie();
                    nouveauType.setLibelle(typeLibelle);
                    return typeService.save(nouveauType);
                });

        Chambre transaction = new Chambre();
        transaction.setMontant(montant);
        transaction.setType(type);
        compte.addTransaction(transaction);

        compteService.save(compte);
        return transactionMapper.toDto(transaction);
    }

    private void effectuerDepot(Reservation compte, long montant) {
        if (montant < DEPOT_MINIMUM) {
            throw new TransactionException(
                    "Le depot minimum est de " + DEPOT_MINIMUM + " FCFA. Montant fourni: " + montant);
        }

        compte.setSolde(compte.getSolde() + montant);
    }

    private void effectuerRetrait(Reservation compte, long montant) {
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

    private <T> T getOrThrow(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(message));
    }
}
