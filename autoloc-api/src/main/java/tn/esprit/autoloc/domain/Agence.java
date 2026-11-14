package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    // Étape 11 : effectif de l'agence
    @Column(name = "effectif")
    private int effectif;

    @OneToMany(mappedBy = "agence", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("agence")
    private Set<Vehicule> vehicules = new HashSet<>();

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("agence")
    private Set<Employe> employes = new HashSet<>();

    // Étape 10 : mettre à jour les deux côtés de la relation en un seul appel
    public void addEmploye(Employe employe) {
        employes.add(employe);
        employe.setAgence(this);   // l'autre côté (celui qui porte la clé étrangère)
    }

    public void removeEmploye(Employe employe) {
        employes.remove(employe);
        employe.setAgence(null);
    }
}