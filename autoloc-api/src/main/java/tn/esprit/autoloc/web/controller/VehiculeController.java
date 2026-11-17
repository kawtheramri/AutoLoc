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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@SuppressWarnings("java:S4684")
@RestController
@RequestMapping("/vehicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(@Qualifier("vehiculeServiceImpl") IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicule findById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    // Ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@Valid @RequestBody Vehicule vehicule) {
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return vehiculeService.save(vehicule);
    }

    // Modification
    @PutMapping("/{id}")
    public Vehicule update(@PathVariable Long id, @Valid @RequestBody Vehicule vehicule) {
        vehiculeService.findById(id);          // lève l'exception "introuvable" → 404
        vehicule.setIdVehicule(id);            // l'id du chemin gagne sur celui du corps
        return vehiculeService.save(vehicule);
    }

    // Suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    // Étape 2 : désaffecter le véhicule de son agence
    @DeleteMapping("/{id}/agence")
    public Vehicule desaffecter(@PathVariable Long id) {
        return vehiculeService.desaffecter(id);
    }

    // Étape 5 : affecter le véhicule à une agence
    @PutMapping("/{idVehicule}/agence/{idAgence}")
    public Vehicule affecterAgence(@PathVariable Long idVehicule, @PathVariable Long idAgence) {
        return vehiculeService.affecterAgence(idVehicule, idAgence);
    }

    @PutMapping("/{idVehicule}/equipements/{idEquipement}")
    public Vehicule affecterEquipement(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        return vehiculeService.affecterEquipement(idVehicule, idEquipement);
    }
}
