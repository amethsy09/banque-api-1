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
@Table(name = "chambre")
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
     @JoinColumn(name = "categorie_id", nullable = false)
    @Positive(message = "Le champ prix doit être positif.")
    private long prix;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
     @Column(unique = true, nullable = false)
    @NotBlank(message = "Le champ nom est obligatoire.")
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categorie_id", nullable = false)
    @NotNull(message = "Le champ categorie est obligatoire.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Categorie type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_id", nullable = false)
    @NotNull(message = "Le champ compte est obligatoire.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Reservation reservation;

    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }
}
