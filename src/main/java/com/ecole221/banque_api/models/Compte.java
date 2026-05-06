package com.ecole221.banque_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comptes")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String numero;

    @Column(nullable = false)
    private long solde = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Le client est obligatoire")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Client client;

    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        transaction.setCompte(this);
    }

    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDate.now();
        }
    }
}
