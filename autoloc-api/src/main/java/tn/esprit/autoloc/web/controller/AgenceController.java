package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceEmployeService;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.service.IAgenceSimulation;

import java.util.List;

@SuppressWarnings("java:S4684")
@RestController
@RequestMapping("/agences")
public class AgenceController {

    private final IAgenceService agenceService;
    private final IAgenceSimulation simulation;
    private final IAgenceEmployeService agenceEmployes;

    public AgenceController(@Qualifier("agenceServiceImpl") IAgenceService agenceService,
                            @Qualifier("agenceServiceImpl") IAgenceSimulation simulation,
                            IAgenceEmployeService agenceEmployes) {
        this.agenceService = agenceService;
        this.simulation = simulation;
        this.agenceEmployes = agenceEmployes;
    }

    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @GetMapping("/{id}")
    public Agence findById(@PathVariable Long id) {
        return agenceService.findById(id);
    }

    @GetMapping("/{id}/simuler")
    public String simuler(@PathVariable Long id,
                          @RequestParam("jours") int jours,
                          @RequestParam("offre") VehiculeOffre offre) {
        Agence agence = agenceService.findById(id);   // 404 si l'agence n'existe pas
        return simulation.simuler(agence, jours, offre);
    }

    // Étape 21 : ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agence create(@Valid @RequestBody Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return agenceService.save(agence);
    }

    // Étape 24 : modification
    @PutMapping("/{id}")
    public Agence update(@PathVariable Long id, @Valid @RequestBody Agence agence) {
        agenceService.findById(id);          // lève l'exception "introuvable" → 404
        agence.setIdAgence(id);              // l'id du chemin gagne sur celui du corps
        return agenceService.save(agence);
    }

    // Étape 12 : affecter les employés d'une agence (pas de @Valid, voir étape 16)
    @PutMapping("/{id}/employes")
    public Agence ajouterEmployes(@PathVariable Long id, @RequestBody Agence agence) {
        return agenceEmployes.ajouterEmployes(id, agence);
    }

    // Étape 25 : suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        agenceService.deleteById(id);
    }
}
