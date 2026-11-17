package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceImplTests {

    private static final long ID_MAINTENANCE = 5L;

    @Mock
    private IMaintenanceRepository maintenanceRepository;

    private MaintenanceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MaintenanceServiceImpl(maintenanceRepository);
    }

    private Vehicule vehicule() {
        return Vehicule.builder()
                .idVehicule(1L)
                .immatriculation("785414TU")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal("100"))
                .build();
    }

    private Maintenance maintenanceValide() {
        return Maintenance.builder()
                .dateDebut(LocalDate.now())
                .description("Vidange et freins")
                .vehicule(vehicule())
                .build();
    }

    @Test
    void createAvecMaintenanceValide() {
        Maintenance maintenance = maintenanceValide();
        when(maintenanceRepository.save(maintenance)).thenReturn(maintenance);

        assertSame(maintenance, service.create(maintenance));
        verify(maintenanceRepository).save(maintenance);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Maintenance maintenance = maintenanceValide();
        maintenance.setIdMaintenance(ID_MAINTENANCE);

        assertThrows(IllegalArgumentException.class, () -> service.create(maintenance));
        verify(maintenanceRepository, never()).save(any());
    }

    @Test
    void createAvecVehiculeManquantEstRefuse() {
        Maintenance maintenance = maintenanceValide();
        maintenance.setVehicule(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(maintenance));
        verify(maintenanceRepository, never()).save(any());
    }

    @Test
    void findByIdAvecMaintenanceExistante() {
        Maintenance maintenance = maintenanceValide();
        when(maintenanceRepository.findById(ID_MAINTENANCE)).thenReturn(Optional.of(maintenance));

        assertSame(maintenance, service.findById(ID_MAINTENANCE));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(maintenanceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecMaintenanceValide() {
        Maintenance existante = maintenanceValide();
        existante.setIdMaintenance(ID_MAINTENANCE);
        Maintenance modifiee = Maintenance.builder()
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(2))
                .description("Revision complete")
                .vehicule(existante.getVehicule())
                .build();
        when(maintenanceRepository.findById(ID_MAINTENANCE)).thenReturn(Optional.of(existante));
        when(maintenanceRepository.save(existante)).thenReturn(existante);

        Maintenance updated = service.update(ID_MAINTENANCE, modifiee);

        assertEquals("Revision complete", existante.getDescription());
        assertEquals(LocalDate.now().plusDays(2), existante.getDateFin());
        assertSame(existante, updated);
    }

    @Test
    void deleteByIdAvecMaintenanceExistante() {
        when(maintenanceRepository.existsById(ID_MAINTENANCE)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_MAINTENANCE));
        verify(maintenanceRepository).deleteById(ID_MAINTENANCE);
    }
}
