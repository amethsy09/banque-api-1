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
    @NotBlank(message = "Le champ numPiece est obligatoire.")
    @Pattern(regexp = "^\\d{13}$", message = "Le champ numPiece doit contenir exactement 13 chiffres.")
    private String numPiece;

    @Column(nullable = false)
    @NotBlank(message = "Le champ prenom est obligatoire.")
    private String prenom;

    @Column(nullable = false)
    @NotBlank(message = "Le champ nom est obligatoire.")
    private String nom;

    private String adresse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Le champ dateNaissance doit être une date passée.")
    private LocalDate dateNaissance;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "Le champ telephone est obligatoire.")
    @Pattern(
            regexp = "^(?:((70|75|76|77|78)\\d{7})|((70|75|76|77|78) \\d{3} \\d{2} \\d{2})|(\\+221 (70|75|76|77|78) \\d{3} \\d{2} \\d{2}))$",
            message = "Le champ telephone doit être au format sénégalais valide : 7XXXXXXXX, 7X XXX XX XX ou +221 7X XXX XX XX."
    )
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
