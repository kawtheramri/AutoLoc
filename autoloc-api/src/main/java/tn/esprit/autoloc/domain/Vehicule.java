package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    private String marque;
    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    @ManyToOne
    private Agence agence;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany(mappedBy = "vehicules")
    private Set<Equipement> equipements = new HashSet<>();
}