package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IReservationRepository;

@Service
public class ReservationServiceImpl extends GenericServiceImpl<Reservation, Long> implements IReservationService {

    public ReservationServiceImpl(IReservationRepository reservationRepository) {
        super(reservationRepository);
    }

    @Override
    protected String getEntityName() {
        return "Reservation";
    }

    @Override
    protected Long getId(Reservation reservation) {
        return reservation.getIdReservation();
    }

    @Override
    protected void setFields(Reservation existing, Reservation updated) {
        existing.setDateDebut(updated.getDateDebut());
        existing.setDateFin(updated.getDateFin());
        existing.setStatut(updated.getStatut());
        existing.setVehicule(updated.getVehicule());
        existing.setClient(updated.getClient());
        existing.setContrat(updated.getContrat());
    }

    @Override
    public boolean check(Reservation reservation, boolean isNew) {
        if (reservation == null) {
            return false;
        }
        return reservation.getDateDebut() != null
                && reservation.getDateFin() != null
                && reservation.getStatut() != null
                && reservation.getVehicule() != null
                && reservation.getClient() != null;
    }
}
