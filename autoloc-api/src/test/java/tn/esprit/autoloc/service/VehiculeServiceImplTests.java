package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.calcul.LongueDureeStrategie;
import tn.esprit.autoloc.calcul.StandardStrategie;
import tn.esprit.autoloc.calcul.StrategieOffres;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.calcul.WeekendStrategie;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTests {

    private static final long ID_VEHICULE = 1L;

    @Mock
    private IVehiculeRepository vehiculeRepository;

    @Mock
    private IAgenceRepository agenceRepository;

    @Mock
    private IEquipementRepository equipementRepository;

    private VehiculeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VehiculeServiceImpl(vehiculeRepository, agenceRepository, equipementRepository,
                new StrategieOffres(List.of(new StandardStrategie(), new WeekendStrategie(), new LongueDureeStrategie())));
    }

    private Vehicule vehicule(String tarif) {
        return Vehicule.builder()
                .idVehicule(ID_VEHICULE)
                .immatriculation("785414TU")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal(tarif))
                .build();
    }

    @Test
    void calculTarifStandardSansRemise() {
        assertEquals(300.0, service.calculTarif(vehicule("100"), 3, VehiculeOffre.STANDARD), 0.001);
    }

    @Test
    void calculTarifWeekendAvecRemise() {
        assertEquals(240.0, service.calculTarif(vehicule("100"), 3, VehiculeOffre.WEEKEND), 0.001);
    }

    @Test
    void calculTarifLongueDureeAvecRemiseSupplementaire() {
        // 100 x 30 = 3000 -> -25 % = 2250 -> > 2000 -> -5 % = 2137.5
        assertEquals(2137.5, service.calculTarif(vehicule("100"), 30, VehiculeOffre.LONGUE_DUREE), 0.001);
    }

    @Test
    void calculTarifLongueDureeSansRemiseSupplementaire() {
        // 80 x 30 = 2400 -> -25 % = 1800 -> pas de -5 %
        assertEquals(1800.0, service.calculTarif(vehicule("80"), 30, VehiculeOffre.LONGUE_DUREE), 0.001);
    }

    @Test
    void desaffecteLeVehiculeDeSonAgence() {
        Vehicule vehicule = vehicule("100");
        vehicule.setAgence(Agence.builder().idAgence(2L).nom("Agence ariana").build());
        when(vehiculeRepository.findById(ID_VEHICULE)).thenReturn(Optional.of(vehicule));

        Vehicule result = service.desaffecter(ID_VEHICULE);

        assertNull(result.getAgence());
        // Entite geree : pas de save explicite
        verify(vehiculeRepository, never()).save(any());
    }

    @Test
    void affecterAgenceAssocieLeVehiculeAAgence() {
        Vehicule vehicule = vehicule("100");
        Agence agence = Agence.builder().idAgence(2L).nom("Agence Lac").build();
        when(vehiculeRepository.findById(ID_VEHICULE)).thenReturn(Optional.of(vehicule));
        when(agenceRepository.findById(2L)).thenReturn(Optional.of(agence));

        Vehicule result = service.affecterAgence(ID_VEHICULE, 2L);

        assertSame(agence, result.getAgence());
        verify(vehiculeRepository, never()).save(any());
    }

    @Test
    void affecterAgenceIntrouvableLeveResourceNotFound() {
        when(vehiculeRepository.findById(ID_VEHICULE)).thenReturn(Optional.of(vehicule("100")));
        when(agenceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecterAgence(ID_VEHICULE, 999L));
    }

    @Test
    void affecterEquipementSynchroniseLesDeuxCotes() {
        Vehicule vehicule = vehicule("100");
        vehicule.setEquipements(new HashSet<>());
        Equipement equipement = Equipement.builder().idEquipement(3L).libelle("GPS").build();
        equipement.setVehicules(new HashSet<>());
        when(vehiculeRepository.findById(ID_VEHICULE)).thenReturn(Optional.of(vehicule));
        when(equipementRepository.findById(3L)).thenReturn(Optional.of(equipement));

        Vehicule result = service.affecterEquipement(ID_VEHICULE, 3L);

        assertTrue(result.getEquipements().contains(equipement));
        assertTrue(equipement.getVehicules().contains(vehicule));
    }

    @Test
    void affecterEquipementIntrouvableLeveResourceNotFound() {
        when(vehiculeRepository.findById(ID_VEHICULE)).thenReturn(Optional.of(vehicule("100")));
        when(equipementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecterEquipement(ID_VEHICULE, 999L));
    }

    @Test
    void createAvecVehiculeValide() {
        Vehicule vehicule = vehicule("100");
        vehicule.setIdVehicule(null);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);

        assertSame(vehicule, service.create(vehicule));
    }

    @Test
    void createAvecVerificationEchouee() {
        Vehicule vehicule = vehicule("100");
        vehicule.setStatut(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(vehicule));
        verify(vehiculeRepository, never()).save(any());
    }
}
