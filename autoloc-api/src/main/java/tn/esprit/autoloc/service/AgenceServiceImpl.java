package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.io.RapportFactory;
import tn.esprit.autoloc.io.RapportTexte;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService, IAgenceSimulation, IAgenceEmployeService {

    private final IVehiculeService vehiculeService;
    private final IEmployeService employeService;

    public AgenceServiceImpl(IAgenceRepository agenceRepository,
                             IVehiculeService vehiculeService,
                             IEmployeService employeService) {
        super(agenceRepository);
        this.vehiculeService = vehiculeService;
        this.employeService = employeService;
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

    // Étape 12 (le @Transactional viendra à l'étape 17)
    @Override
    public Agence ajouterEmployes(Long idAgence, Agence donnees) {
        Agence agence = findById(idAgence);

        // 1. mettre à jour l'agence d'abord
        agence.setNom(donnees.getNom());
        agence.setAdresse(donnees.getAdresse());
        agence.setTelephone(donnees.getTelephone());
        agence.setVille(donnees.getVille());
        save(agence);

        // 2. insérer les employés avec le service des employés
        List<Employe> nouveaux = new ArrayList<>(donnees.getEmployes());
        for (Employe e : nouveaux) {
            agence.addEmploye(e);
            employeService.save(e);
        }

        // 3. effectif = taille de la liste
        agence.setEffectif(nouveaux.size());
        return save(agence);
    }
}
