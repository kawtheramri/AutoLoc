package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.io.RapportFactory;
import tn.esprit.autoloc.io.RapportTexte;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService, IAgenceSimulation {

    private final IVehiculeService vehiculeService;

    public AgenceServiceImpl(IAgenceRepository agenceRepository, IVehiculeService vehiculeService) {
        super(agenceRepository);
        this.vehiculeService = vehiculeService;
    }

    @Override
    protected String getEntityName() {
        return "Agence";
    }

    @Override
    protected Long getId(Agence agence) {
        return agence.getIdAgence();
    }

    @Override
    protected void setFields(Agence existing, Agence updated) {
        existing.setNom(updated.getNom());
        existing.setAdresse(updated.getAdresse());
        existing.setTelephone(updated.getTelephone());
        existing.setVille(updated.getVille());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        if (agence == null) {
            return false;
        }
        return Utils.isNotEmpty(agence.getNom())
                && Utils.isNotEmpty(agence.getAdresse())
                && Utils.isNotEmpty(agence.getTelephone())
                && Utils.isNotEmpty(agence.getVille());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }

    @Override
    public String simuler(Agence agence, int nbJours, VehiculeOffre offre) {
        RapportTexte rapport = RapportFactory.creerRapport();

        rapport.ecrireEntete(agence);   // affiche l'entête sur la console et la capture

        for (Vehicule vehicule : agence.getVehicules()) {
            rapport.ajouter(vehiculeService.calculTarif(vehicule, nbJours, offre));
        }

        rapport.ecrireTotal(rapport.getTotal());   // le rapport retourne le total

        return rapport.contenu();
    }
}
