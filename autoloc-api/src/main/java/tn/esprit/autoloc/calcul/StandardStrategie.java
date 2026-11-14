package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Vehicule;

@Component
public class StandardStrategie implements IOffreStrategie {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.STANDARD;
    }

    @Override
    public double calculerTarif(Vehicule vehicule, int jours) {
        return vehicule.getTarifJournalier().doubleValue() * jours;
    }
}
