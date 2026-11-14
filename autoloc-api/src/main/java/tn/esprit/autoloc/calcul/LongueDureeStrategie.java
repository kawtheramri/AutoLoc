package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Vehicule;

@Component
public class LongueDureeStrategie implements IOffreStrategie {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.LONGUE_DUREE;
    }

    @Override
    public double calculerTarif(Vehicule vehicule, int jours) {
        double total = vehicule.getTarifJournalier().doubleValue() * 0.75 * jours;   // -25 %
        return total > 2000 ? total * 0.95 : total;                                  // -5 % si > 2000
    }
}
