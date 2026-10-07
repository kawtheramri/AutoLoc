package tn.esprit.autoloc.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    private String libelle;

    @ManyToMany
    @JoinTable(
            name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "id_equipement"),
            inverseJoinColumns = @JoinColumn(name = "id_vehicule")
    )
    private Set<Vehicule> vehicules = new HashSet<>();
}
