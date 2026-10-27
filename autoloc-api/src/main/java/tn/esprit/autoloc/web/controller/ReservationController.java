package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final IReservationService reservationService;

    public ReservationController(@Qualifier("reservationServiceImpl") IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public Reservation findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    // Ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@Valid @RequestBody Reservation reservation) {
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return reservationService.save(reservation);
    }

    // Modification
    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @Valid @RequestBody Reservation reservation) {
        reservationService.findById(id);          // lève l'exception "introuvable" → 404
        reservation.setIdReservation(id);        // l'id du chemin gagne sur celui du corps
        return reservationService.save(reservation);
    }

    // Suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
