package tn.esprit.autoloc.service;

import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService extends IGenericService<Vehicule, Long> {

    double calculTarif(Vehicule vehicule, int jours, VehiculeOffre offre);

    Vehicule desaffecter(Long idVehicule);

    Vehicule affecterAgence(Long idVehicule, Long idAgence);

    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);
}
