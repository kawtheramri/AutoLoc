package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipementServiceImplTests {

    private static final long ID_EQUIPEMENT = 5L;

    @Mock
    private IEquipementRepository equipementRepository;

    private EquipementServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EquipementServiceImpl(equipementRepository);
    }

    private Equipement equipementValide() {
        return Equipement.builder()
                .libelle("GPS")
                .build();
    }

    @Test
    void createAvecEquipementValide() {
        Equipement equipement = equipementValide();
        when(equipementRepository.save(equipement)).thenReturn(equipement);

        assertSame(equipement, service.create(equipement));
        verify(equipementRepository).save(equipement);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Equipement equipement = equipementValide();
        equipement.setIdEquipement(ID_EQUIPEMENT);

        assertThrows(IllegalArgumentException.class, () -> service.create(equipement));
        verify(equipementRepository, never()).save(any());
    }

    @Test
    void createAvecLibelleVideEstRefuse() {
        Equipement equipement = equipementValide();
        equipement.setLibelle("   ");

        assertThrows(IllegalArgumentException.class, () -> service.create(equipement));
        verify(equipementRepository, never()).save(any());
    }

    @Test
    void findByIdAvecEquipementExistant() {
        Equipement equipement = equipementValide();
        when(equipementRepository.findById(ID_EQUIPEMENT)).thenReturn(Optional.of(equipement));

        assertSame(equipement, service.findById(ID_EQUIPEMENT));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(equipementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecEquipementValide() {
        Equipement existant = equipementValide();
        existant.setIdEquipement(ID_EQUIPEMENT);
        Equipement modifie = Equipement.builder().libelle("Toit ouvrant").build();
        when(equipementRepository.findById(ID_EQUIPEMENT)).thenReturn(Optional.of(existant));
        when(equipementRepository.save(existant)).thenReturn(existant);

        Equipement updated = service.update(ID_EQUIPEMENT, modifie);

        assertEquals("Toit ouvrant", existant.getLibelle());
        assertSame(existant, updated);
    }

    @Test
    void deleteByIdAvecEquipementExistant() {
        when(equipementRepository.existsById(ID_EQUIPEMENT)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_EQUIPEMENT));
        verify(equipementRepository).deleteById(ID_EQUIPEMENT);
    }
}
