package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratServiceImplTests {

    private static final long ID_CONTRAT = 5L;

    @Mock
    private IContratRepository contratRepository;

    private ContratServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContratServiceImpl(contratRepository);
    }

    private Contrat contratValide() {
        return Contrat.builder()
                .dateSignature(LocalDate.now())
                .montantTotal(new BigDecimal("1500.000"))
                .valide(Boolean.TRUE)
                .build();
    }

    @Test
    void createAvecContratValide() {
        Contrat contrat = contratValide();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        assertSame(contrat, service.create(contrat));
        verify(contratRepository).save(contrat);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Contrat contrat = contratValide();
        contrat.setIdContrat(ID_CONTRAT);

        assertThrows(IllegalArgumentException.class, () -> service.create(contrat));
        verify(contratRepository, never()).save(any());
    }

    @Test
    void createAvecValidationManquanteEstRefuse() {
        Contrat contrat = contratValide();
        contrat.setValide(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(contrat));
        verify(contratRepository, never()).save(any());
    }

    @Test
    void findByIdAvecContratExistant() {
        Contrat contrat = contratValide();
        when(contratRepository.findById(ID_CONTRAT)).thenReturn(Optional.of(contrat));

        assertSame(contrat, service.findById(ID_CONTRAT));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(contratRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecContratValide() {
        Contrat existant = contratValide();
        existant.setIdContrat(ID_CONTRAT);
        Contrat modifie = Contrat.builder()
                .dateSignature(LocalDate.now())
                .montantTotal(new BigDecimal("1800.000"))
                .valide(Boolean.TRUE)
                .build();
        when(contratRepository.findById(ID_CONTRAT)).thenReturn(Optional.of(existant));
        when(contratRepository.save(existant)).thenReturn(existant);

        Contrat updated = service.update(ID_CONTRAT, modifie);

        assertEquals(new BigDecimal("1800.000"), existant.getMontantTotal());
        assertSame(existant, updated);
    }

    @Test
    void deleteByIdAvecContratExistant() {
        when(contratRepository.existsById(ID_CONTRAT)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_CONTRAT));
        verify(contratRepository).deleteById(ID_CONTRAT);
    }
}
