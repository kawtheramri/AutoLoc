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
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@SuppressWarnings("java:S4684")
@RestController
@RequestMapping("/equipements")
public class EquipementController {

    private final IEquipementService equipementService;

    public EquipementController(@Qualifier("equipementServiceImpl") IEquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @GetMapping
    public List<Equipement> findAll() {
        return equipementService.findAll();
    }

    @GetMapping("/{id}")
    public Equipement findById(@PathVariable Long id) {
        return equipementService.findById(id);
    }

    // Ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipement create(@Valid @RequestBody Equipement equipement) {
        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return equipementService.save(equipement);
    }

    // Modification
    @PutMapping("/{id}")
    public Equipement update(@PathVariable Long id, @Valid @RequestBody Equipement equipement) {
        equipementService.findById(id);          // lève l'exception "introuvable" → 404
        equipement.setIdEquipement(id);         // l'id du chemin gagne sur celui du corps
        return equipementService.save(equipement);
    }

    // Suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        equipementService.deleteById(id);
    }
}
