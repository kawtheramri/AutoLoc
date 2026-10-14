package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    public VehiculeServiceImpl(IVehiculeRepository vehiculeRepository) {
        super(vehiculeRepository);
    }

    @Override
    protected String getEntityName() {
        return "Vehicule";
    }

    @Override
    protected Long getId(Vehicule vehicule) {
        return vehicule.getIdVehicule();
    }

    @Override
    protected void setFields(Vehicule existing, Vehicule updated) {
        existing.setImmatriculation(updated.getImmatriculation());
        existing.setMarque(updated.getMarque());
        existing.setModele(updated.getModele());
        existing.setCategorie(updated.getCategorie());
        existing.setTarifJournalier(updated.getTarifJournalier());
        existing.setStatut(updated.getStatut());
        existing.setAgence(updated.getAgence());
    }

    @Override
    public boolean check(Vehicule vehicule, boolean isNew) {
        if (vehicule == null) {
            return false;
        }
        return Utils.isNotEmpty(vehicule.getImmatriculation())
                && Utils.isNotEmpty(vehicule.getMarque())
                && Utils.isNotEmpty(vehicule.getModele())
                && vehicule.getCategorie() != null
                && vehicule.getTarifJournalier() != null
                && vehicule.getStatut() != null;
    }
}
