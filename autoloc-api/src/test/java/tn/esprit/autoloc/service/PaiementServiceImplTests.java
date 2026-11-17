package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaiementServiceImplTests {

    // Le paiement est en lecture seule : uniquement findAll et findById

    @Mock
    private IPaiementRepository paiementRepository;

    private PaiementServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PaiementServiceImpl(paiementRepository);
    }

    @Test
    void findAllRetourneLesPaiements() {
        Paiement paiement = Paiement.builder()
                .idPaiement(1L)
                .montant(new java.math.BigDecimal("540.000"))
                .modePaiement(ModePaiement.CARTE)
                .build();
        when(paiementRepository.findAll()).thenReturn(List.of(paiement));

        assertEquals(1, service.findAll().size());
    }

    @Test
    void findByIdAvecPaiementExistant() {
        Paiement paiement = Paiement.builder().idPaiement(1L).build();
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));

        assertSame(paiement, service.findById(1L));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(paiementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void laMiseAJourEstInterdite() {
        Paiement paiement = Paiement.builder().idPaiement(1L).build();
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));

        assertThrows(UnsupportedOperationException.class,
                () -> service.update(1L, Paiement.builder().build()));
        verify(paiementRepository, never()).save(any());
    }
}
