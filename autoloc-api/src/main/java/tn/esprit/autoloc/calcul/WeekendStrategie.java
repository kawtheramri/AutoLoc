package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Vehicule;

@Component
public class WeekendStrategie implements IOffreStrategie {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.WEEKEND;
    }

    @Override
    public double calculerTarif(Vehicule vehicule, int jours) {
        return vehicule.getTarifJournalier().doubleValue() * 0.80 * jours;   // -20 %
    }
}
