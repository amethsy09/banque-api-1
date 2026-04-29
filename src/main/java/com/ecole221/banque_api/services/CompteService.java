package com.ecole221.banque_api.services;

import com.ecole221.banque_api.dto.CompteCreateDto;
import com.ecole221.banque_api.dto.CompteDto;
import com.ecole221.banque_api.dto.TransactionDto;
import com.ecole221.banque_api.dto.TransactionRequestDto;
import com.ecole221.banque_api.exceptions.ResourceNotFoundException;
import com.ecole221.banque_api.exceptions.TransactionException;
import com.ecole221.banque_api.helpers.NumeroCompteHelper;
import com.ecole221.banque_api.mappers.CompteMapper;
import com.ecole221.banque_api.mappers.TransactionMapper;
import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.models.Compte;
import com.ecole221.banque_api.models.Transaction;
import com.ecole221.banque_api.models.Type;
import com.ecole221.banque_api.repository.ClientRepository;
import com.ecole221.banque_api.repository.CompteRepository;
import com.ecole221.banque_api.repository.TransactionRepository;
import com.ecole221.banque_api.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompteService {

    // Contraintes métier
    private static final long DEPOT_MINIMUM = 10_000L;
    private static final long SOLDE_MINIMUM = -50_000L;
    private static final long SOLDE_MAXIMUM = 50_000L;

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final TypeRepository typeRepository;
    private final CompteMapper compteMapper;
    private final TransactionMapper transactionMapper;
    private final NumeroCompteHelper numeroCompteHelper;

    /**
     * Créer un nouveau compte pour un client existant.
     */
    public CompteDto creerCompte(CompteCreateDto dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id: " + dto.getClientId()));

        Compte compte = new Compte();
        compte.setClient(client);
        compte.setSolde(dto.getSoldeInitial());
        compte.setNumero(genererNumeroUnique());

        return compteMapper.toDto(compteRepository.save(compte));
    }

    /**
     * Lister tous les comptes d'un client.
     */
    @Transactional(readOnly = true)
    public List<CompteDto> listerComptesDuClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client introuvable avec l'id: " + clientId);
        }
        return compteRepository.findByClientId(clientId).stream()
                .map(compteMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Rechercher un compte par son numéro.
     */
    @Transactional(readOnly = true)
    public CompteDto rechercherParNumero(String numero) {
        return compteMapper.toDto(
                compteRepository.findByNumero(numero)
                        .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable avec le numéro: " + numero))
        );
    }

    /**
     * Afficher les infos d'un compte par son ID.
     */
    @Transactional(readOnly = true)
    public CompteDto afficherInfos(Long id) {
        return compteMapper.toDto(
                compteRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable avec l'id: " + id))
        );
    }

    /**
     * Effectuer une transaction (dépôt ou retrait) avec les contraintes métier.
     *
     * Règles :
     *  - Dépôt minimum : 10 000
     *  - Le solde ne peut pas descendre en dessous de -50 000
     *  - Le solde ne peut pas dépasser 50 000 (ex: si solde = 60000, retrait max = 10000)
     */
    public TransactionDto effectuerTransaction(TransactionRequestDto request) {
        Compte compte = compteRepository.findByNumero(request.getCompteNumero())
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable: " + request.getCompteNumero()));

        String typeLibelle = request.getType().toUpperCase().trim();
        long montant = request.getMontant();

        switch (typeLibelle) {
            case "DEPOT" -> effectuerDepot(compte, montant);
            case "RETRAIT" -> effectuerRetrait(compte, montant);
            default -> throw new TransactionException("Type de transaction invalide. Utiliser DEPOT ou RETRAIT.");
        }

        compteRepository.save(compte);

        Type type = typeRepository.findByLibelleIgnoreCase(typeLibelle)
                .orElseGet(() -> {
                    Type t = new Type();
                    t.setLibelle(typeLibelle);
                    return typeRepository.save(t);
                });

        Transaction transaction = new Transaction();
        transaction.setMontant(montant);
        transaction.setCompte(compte);
        transaction.setType(type);

        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    // ─── Logique métier privée ───────────────────────────────────────────────

    private void effectuerDepot(Compte compte, long montant) {
        if (montant < DEPOT_MINIMUM) {
            throw new TransactionException(
                    "Le dépôt minimum est de " + DEPOT_MINIMUM + " FCFA. Montant fourni: " + montant);
        }
        long nouveauSolde = compte.getSolde() + montant;
        compte.setSolde(nouveauSolde);
    }

    private void effectuerRetrait(Compte compte, long montant) {
        long soldeActuel = compte.getSolde();
        long soldeApresRetrait = soldeActuel - montant;

        // Le solde ne peut pas descendre en dessous de -50 000
        if (soldeApresRetrait < SOLDE_MINIMUM) {
            throw new TransactionException(
                    "Retrait refusé. Le solde ne peut pas être inférieur à " + SOLDE_MINIMUM + " FCFA. "
                            + "Solde actuel: " + soldeActuel + " FCFA.");
        }

        // Si le solde dépasse 50 000, le retrait maximum autorisé ramène à 50 000
        if (soldeActuel > SOLDE_MAXIMUM) {
            long retraitMax = soldeActuel - SOLDE_MAXIMUM;
            throw new TransactionException(
                    "Votre solde dépasse " + SOLDE_MAXIMUM + " FCFA (solde actuel: " + soldeActuel + " FCFA). "
                            + "Vous ne pouvez retirer que " + retraitMax + " FCFA maximum.");
        }

        compte.setSolde(soldeApresRetrait);
    }

    private String genererNumeroUnique() {
        String numero;
        do {
            numero = numeroCompteHelper.generer();
        } while (compteRepository.existsByNumero(numero));
        return numero;
    }
}
