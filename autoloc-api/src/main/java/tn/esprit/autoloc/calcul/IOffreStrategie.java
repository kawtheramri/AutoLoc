package tn.esprit.autoloc.calcul;

import tn.esprit.autoloc.domain.Vehicule;

public interface IOffreStrategie {

    VehiculeOffre getOffre();

    double calculerTarif(Vehicule vehicule, int jours);
}
