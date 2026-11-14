package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.StrategieOffres;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final StrategieOffres strategies;

    public VehiculeServiceImpl(IVehiculeRepository vehiculeRepository,
                               IAgenceRepository agenceRepository,
                               IEquipementRepository equipementRepository,
                               StrategieOffres strategies) {
        super(vehiculeRepository);
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
        this.strategies = strategies;
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

    @Override
    public double calculTarif(Vehicule vehicule, int jours, VehiculeOffre offre) {
        return strategies.get(offre).calculerTarif(vehicule, jours);
    }

    // Étape 1 : minimaliste
    @Override
    @Transactional
    public Vehicule desaffecter(Long idVehicule) {
        Vehicule v = findById(idVehicule);
        v.setAgence(null);
        return v;
    }

    // Étape 4
    @Override
    @Transactional
    public Vehicule affecterAgence(Long idVehicule, Long idAgence) {
        Vehicule v = findById(idVehicule);
        v.setAgence(agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", String.valueOf(idAgence))));
        return v;
    }

    // Étape 6
    @Override
    @Transactional
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule v = findById(idVehicule);
        Equipement e = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", String.valueOf(idEquipement)));
        v.getEquipements().add(e);
        e.getVehicules().add(v);      // synchronise l'autre côté (celui qui porte la table de jointure)
        return v;
    }
}
