package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgenceServiceImplTests {

    private static final long ID_AGENCE = 5L;

    @Mock
    private IAgenceRepository agenceRepository;

    @Mock
    private IVehiculeService vehiculeService;

    @Mock
    private IEmployeService employeService;

    private AgenceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AgenceServiceImpl(agenceRepository, vehiculeService, employeService);
    }

    private Agence agenceValide() {
        return Agence.builder()
                .nom("Agence ariana")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
                .build();
    }

    private Vehicule vehicule(String immatriculation, String tarif) {
        return Vehicule.builder()
                .immatriculation(immatriculation)
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal(tarif))
                .build();
    }

    @Test
    void createAvecAgenceValide() {
        Agence agence = agenceValide();
        when(agenceRepository.save(agence)).thenReturn(agence);

        assertSame(agence, service.create(agence));
        verify(agenceRepository).save(agence);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Agence agence = Agence.builder().idAgence(1L).nom("Agence ariana").build();

        assertThrows(IllegalArgumentException.class, () -> service.create(agence));
        verify(agenceRepository, never()).save(any());
    }

    @Test
    void createAvecVerificationEchouee() {
        Agence agence = Agence.builder().nom("Agence ariana").build();

        assertThrows(IllegalArgumentException.class, () -> service.create(agence));
        verify(agenceRepository, never()).save(any());
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(agenceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecAgenceValide() {
        Agence existante = agenceValide();
        existante.setIdAgence(ID_AGENCE);
        Agence modifiee = Agence.builder()
                .nom("Agence Lac")
                .adresse("2 Rue du Lac")
                .telephone("71123456")
                .ville("Ariana")
                .build();
        when(agenceRepository.findById(ID_AGENCE)).thenReturn(Optional.of(existante));
        when(agenceRepository.save(existante)).thenReturn(existante);

        Agence updated = service.update(ID_AGENCE, modifiee);

        assertEquals("Agence Lac", existante.getNom());
        assertEquals("Ariana", existante.getVille());
        assertSame(existante, updated);
    }

    @Test
    void deleteByIdAvecAgenceExistante() {
        when(agenceRepository.existsById(ID_AGENCE)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_AGENCE));
        verify(agenceRepository).deleteById(ID_AGENCE);
    }

    @Test
    void simulerCalculeLeTotalDesVehicules() {
        Agence agence = Agence.builder()
                .idAgence(11L)
                .nom("Agence ariana")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
                .vehicules(Set.of(vehicule("785414TU", "100"), vehicule("785415TU", "80")))
                .build();
        when(vehiculeService.calculTarif(any(Vehicule.class), eq(3), eq(VehiculeOffre.STANDARD)))
                .thenReturn(300.0, 240.0);

        String rapport = service.simuler(agence, 3, VehiculeOffre.STANDARD);

        assertTrue(rapport.startsWith("Id : 11, Nom : Agence ariana, Tel : 71585874\n"));
        assertTrue(rapport.contains("Total tarif : 540.0"));
    }

    @Test
    void ajouterEmployesMetAJourAgenceEmployesEtEffectif() {
        Agence agence = agenceValide();
        agence.setIdAgence(ID_AGENCE);
        agence.setEmployes(new HashSet<>());
        Agence modifiee = Agence.builder()
                .nom("Agence Lac")
                .adresse("2 Rue du Lac")
                .telephone("71123456")
                .ville("Ariana")
                .build();
        Employe e1 = Employe.builder().nom("Ben Ali").prenom("Sami").role(RoleEmploye.CONSEILLER).build();
        Employe e2 = Employe.builder().nom("Trabelsi").prenom("Amel").role(RoleEmploye.CONSEILLER).build();

        when(agenceRepository.findById(ID_AGENCE)).thenReturn(Optional.of(agence));
        when(agenceRepository.save(agence)).thenReturn(agence);
        when(employeService.save(any(Employe.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        modifiee.setEmployes(Set.of(e1, e2));
        Agence result = service.ajouterEmployes(ID_AGENCE, modifiee);

        assertEquals(2, result.getEffectif());
        assertTrue(result.getEmployes().containsAll(List.of(e1, e2)));
        assertEquals("Agence Lac", result.getNom());
        assertEquals("Ariana", result.getVille());
        verify(employeService).save(e1);
        verify(employeService).save(e2);
        verify(agenceRepository, times(2)).save(agence);
    }

    @Test
    void ajouterEmployesSurAgenceIntrouvableLeveResourceNotFound() {
        when(agenceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.ajouterEmployes(999L, Agence.builder().build()));
        verify(employeService, never()).save(any());
    }
}
