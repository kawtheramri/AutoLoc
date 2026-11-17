package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericServiceImplTests {

    @Mock
    private JpaRepository<Agence, Long> agenceRepository;

    private GenericServiceImpl<Agence, Long> service;

    // Sous-classe concrete minimale pour tester la base generique
    private static class AgenceTestService extends GenericServiceImpl<Agence, Long> {

        AgenceTestService(JpaRepository<Agence, Long> repository) {
            super(repository);
        }

        @Override
        protected String getEntityName() {
            return "Agence";
        }

        @Override
        protected Long getId(Agence agence) {
            return agence.getIdAgence();
        }

        @Override
        protected void setFields(Agence existing, Agence updated) {
            existing.setNom(updated.getNom());
            existing.setAdresse(updated.getAdresse());
            existing.setTelephone(updated.getTelephone());
            existing.setVille(updated.getVille());
        }

        @Override
        public boolean check(Agence agence, boolean isNew) {
            return agence != null && Utils.isNotEmpty(agence.getNom());
        }
    }

    @BeforeEach
    void setUp() {
        service = new AgenceTestService(agenceRepository);
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
    void createAvecEntiteValide() {
        Agence agence = agenceValide();
        when(agenceRepository.save(agence)).thenReturn(agence);

        Agence created = service.create(agence);

        assertSame(agence, created);
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
        Agence agence = Agence.builder().adresse("1 Rue Hedi").build();

        assertThrows(IllegalArgumentException.class, () -> service.create(agence));
        verify(agenceRepository, never()).save(any());
    }

    @Test
    void saveSansIdentifiantDelegueVersCreate() {
        Agence agence = agenceValide();
        when(agenceRepository.save(agence)).thenReturn(agence);

        assertSame(agence, service.save(agence));
        verify(agenceRepository).save(agence);
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

        assertSame(existante, saved);
        assertEquals("Agence Lac", existante.getNom());
    }

    @Test
    void findByIdAvecEntiteExistante() {
        Agence agence = agenceValide();
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(agence));

        assertSame(agence, service.findById(5L));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(agenceRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
        assertTrue(exception.getMessage().contains("Agence"));
    }

    @Test
    void findAllRetourneToutesLesEntites() {
        Agence agence = agenceValide();
        when(agenceRepository.findAll()).thenReturn(List.of(agence));

        assertEquals(1, service.findAll().size());
    }

    @Test
    void updateAvecEntiteValide() {
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

        assertSame(existante, updated);
        assertEquals("Agence Lac", existante.getNom());
        assertEquals("2 Rue du Lac", existante.getAdresse());
        assertEquals("Ariana", existante.getVille());
    }

    @Test
    void updateAvecVerificationEchouee() {
        Agence existante = agenceValide();
        existante.setIdAgence(5L);
        when(agenceRepository.findById(5L)).thenReturn(Optional.of(existante));

        assertThrows(IllegalArgumentException.class,
                () -> service.update(5L, Agence.builder().adresse("x").build()));
        verify(agenceRepository, never()).save(any());
    }

    @Test
    void deleteByIdAvecEntiteExistante() {
        when(agenceRepository.existsById(5L)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(5L));
        verify(agenceRepository).deleteById(5L);
    }

    @Test
    void deleteByIdIntrouvableLeveResourceNotFound() {
        when(agenceRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(999L));
        verify(agenceRepository, never()).deleteById(999L);
    }
}
