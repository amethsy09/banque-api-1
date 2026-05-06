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
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Le numéro de pièce est obligatoire")
    private String numPiece;

    @Column(nullable = false)
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Column(nullable = false)
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String adresse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+\\s-]{8,20}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @OneToOne(mappedBy = "client", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AppUser appUser;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<Compte> comptes;

    public void addCompte(Compte compte) {
        if (comptes == null) {
            comptes = new ArrayList<>();
        }
        comptes.add(compte);
        compte.setClient(this);
    }
}
