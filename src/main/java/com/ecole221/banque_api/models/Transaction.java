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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Positive(message = "Le montant doit être positif")
    private long montant;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "type_id", nullable = false)
    @NotNull(message = "Le type de transaction est obligatoire")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_id", nullable = false)
    @NotNull(message = "Le compte est obligatoire")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Compte compte;

    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }
}
