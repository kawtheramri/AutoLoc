package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;
    private String adresse;
    private String telephone;
    private String ville;

    @OneToMany(mappedBy = "agence", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Vehicule> vehicules = new HashSet<>();

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    private Set<Employe> employes = new HashSet<>();
}