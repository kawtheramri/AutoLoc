package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicAgenceServiceImplTests {

    @Mock
    private IAgenceRepository agenceRepository;

    private BasicAgenceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BasicAgenceServiceImpl(agenceRepository);
    }

    private Agence agenceValide() {
        return Agence.builder()
                .nom("Agence ariana")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
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
        Agence agence = Agence.builder().idAgence(5L).nom("Agence ariana").build();

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
    void saveSansIdentifiantDelegueVersCreate() {
        Agence agence = agenceValide();
        when(agenceRepository.save(agence)).thenReturn(agence);

        assertSame(agence, service.save(agence));
    }

    @Test
    void saveAvecIdentifiantDelegueVersUpdate() {
        Agence existante = agenceValide();
        existante.setIdAgence(5L);
        Agence modifiee = Agence.builder()
                .idAgence(5L)
                .nom("Agence Lac")
                .adresse(existante.getAdresse())
                .telephone(existante.getTelephone())
                .ville(existante.getVille())
                .build();
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(existante));
        when(agenceRepository.save(existante)).thenReturn(existante);

        Agence saved = service.save(modifiee);

        assertEquals("Agence Lac", existante.getNom());
        assertSame(existante, saved);
    }

    @Test
    void findByIdAvecAgenceExistante() {
        Agence agence = agenceValide();
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(agence));

        assertSame(agence, service.findById(5L));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(agenceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void findAllNeRetourneJamaisNull() {
        // S1168 : la liste vide remplace le retour null
        assertNotNull(service.findAll());
        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void updateAvecAgenceValide() {
        Agence existante = agenceValide();
        existante.setIdAgence(5L);
        Agence modifiee = Agence.builder()
                .nom("Agence Lac")
                .adresse("2 Rue du Lac")
                .telephone("71123456")
                .ville("Ariana")
                .build();
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(existante));
        when(agenceRepository.save(existante)).thenReturn(existante);

        Agence updated = service.update(5L, modifiee);

        assertEquals("Agence Lac", existante.getNom());
        assertEquals("Ariana", existante.getVille());
        assertSame(existante, updated);
    }

    @Test
    void deleteByIdAvecAgenceExistante() {
        when(agenceRepository.existsById(5L)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(5L));
        verify(agenceRepository).deleteById(5L);
    }

    @Test
    void deleteByIdIntrouvableLeveResourceNotFound() {
        when(agenceRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(999L));
    }

    @Test
    void hasVehiculesEstImplementeBasiquement() {
        // BasicAgenceServiceImpl : pas de gestion des vehicules
        assertFalse(service.hasVehicules(5L));
    }
}
