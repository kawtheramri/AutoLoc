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
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/agences")
public class AgenceController {

    private final IAgenceService agenceService;

    public AgenceController(@Qualifier("agenceServiceImpl") IAgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @GetMapping("/{id}")
    public Agence findById(@PathVariable Long id) {
        return agenceService.findById(id);
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

    // Étape 25 : suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        agenceService.deleteById(id);
    }
}
