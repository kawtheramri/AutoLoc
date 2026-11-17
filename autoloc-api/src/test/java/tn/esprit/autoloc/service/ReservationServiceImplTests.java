package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTests {

    private static final long ID_RESERVATION = 5L;

    @Mock
    private IReservationRepository reservationRepository;

    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReservationServiceImpl(reservationRepository);
    }

    private Reservation reservationValide() {
        Vehicule vehicule = Vehicule.builder()
                .immatriculation("785414TU")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal("100"))
                .build();
        Client client = Client.builder()
                .nom("Ben Ali")
                .prenom("Sami")
                .email("sami.benali@mail.tn")
                .telephone("71234567")
                .numPermis("95123456")
                .build();
        return Reservation.builder()
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(3))
                .statut(StatutReservation.CONFIRMEE)
                .vehicule(vehicule)
                .client(client)
                .build();
    }

    @Test
    void createAvecReservationValide() {
        Reservation reservation = reservationValide();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        assertSame(reservation, service.create(reservation));
        verify(reservationRepository).save(reservation);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Reservation reservation = reservationValide();
        reservation.setIdReservation(ID_RESERVATION);

        assertThrows(IllegalArgumentException.class, () -> service.create(reservation));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createAvecClientManquantEstRefuse() {
        Reservation reservation = reservationValide();
        reservation.setClient(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(reservation));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void findByIdAvecReservationExistante() {
        Reservation reservation = reservationValide();
        when(reservationRepository.findById(ID_RESERVATION)).thenReturn(Optional.of(reservation));

        assertSame(reservation, service.findById(ID_RESERVATION));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecReservationValide() {
        Reservation existante = reservationValide();
        existante.setIdReservation(ID_RESERVATION);
        Reservation modifiee = reservationValide();
        modifiee.setStatut(StatutReservation.ANNULEE);
        when(reservationRepository.findById(ID_RESERVATION)).thenReturn(Optional.of(existante));
        when(reservationRepository.save(existante)).thenReturn(existante);

        Reservation updated = service.update(ID_RESERVATION, modifiee);

        assertEquals(StatutReservation.ANNULEE, existante.getStatut());
        assertSame(existante, updated);
    }

    @Test
    void deleteByIdAvecReservationExistante() {
        when(reservationRepository.existsById(ID_RESERVATION)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_RESERVATION));
        verify(reservationRepository).deleteById(ID_RESERVATION);
    }
}
