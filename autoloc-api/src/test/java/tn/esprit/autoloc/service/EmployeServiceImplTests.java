package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeServiceImplTests {

    private static final long ID_EMPLOYE = 5L;

    @Mock
    private IEmployeRepository employeRepository;

    private EmployeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeServiceImpl(employeRepository);
    }

    private Employe employeValide() {
        return Employe.builder()
                .nom("Ben Ali")
                .prenom("Sami")
                .role(RoleEmploye.CONSEILLER)
                .build();
    }

    @Test
    void createAvecEmployeValide() {
        Employe employe = employeValide();
        when(employeRepository.save(employe)).thenReturn(employe);

        assertSame(employe, service.create(employe));
        verify(employeRepository).save(employe);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Employe employe = employeValide();
        employe.setIdEmploye(ID_EMPLOYE);

        assertThrows(IllegalArgumentException.class, () -> service.create(employe));
        verify(employeRepository, never()).save(any());
    }

    @Test
    void createAvecRoleManquantEstRefuse() {
        Employe employe = employeValide();
        employe.setRole(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(employe));
        verify(employeRepository, never()).save(any());
    }

    @Test
    void findByIdAvecEmployeExistant() {
        Employe employe = employeValide();
        when(employeRepository.findById(ID_EMPLOYE)).thenReturn(Optional.of(employe));

        assertSame(employe, service.findById(ID_EMPLOYE));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(employeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecEmployeValide() {
        Employe existant = employeValide();
        existant.setIdEmploye(ID_EMPLOYE);
        Employe modifie = Employe.builder()
                .nom("Trabelsi")
                .prenom("Amel")
                .role(RoleEmploye.MANAGER)
                .build();
        when(employeRepository.findById(ID_EMPLOYE)).thenReturn(Optional.of(existant));
        when(employeRepository.save(existant)).thenReturn(existant);

        Employe updated = service.update(ID_EMPLOYE, modifie);

        assertEquals("Trabelsi", existant.getNom());
        assertEquals(RoleEmploye.MANAGER, existant.getRole());
        assertSame(existant, updated);
    }

    @Test
    void deleteByIdAvecEmployeExistant() {
        when(employeRepository.existsById(ID_EMPLOYE)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_EMPLOYE));
        verify(employeRepository).deleteById(ID_EMPLOYE);
    }
}
