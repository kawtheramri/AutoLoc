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
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats")
public class ContratController {

    private final IContratService contratService;

    public ContratController(@Qualifier("contratServiceImpl") IContratService contratService) {
        this.contratService = contratService;
    }

    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @GetMapping("/{id}")
    public Contrat findById(@PathVariable Long id) {
        return contratService.findById(id);
    }

    // Ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contrat create(@Valid @RequestBody Contrat contrat) {
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return contratService.save(contrat);
    }

    // Modification
    @PutMapping("/{id}")
    public Contrat update(@PathVariable Long id, @Valid @RequestBody Contrat contrat) {
        contratService.findById(id);          // lève l'exception "introuvable" → 404
        contrat.setIdContrat(id);             // l'id du chemin gagne sur celui du corps
        return contratService.save(contrat);
    }

    // Suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        contratService.deleteById(id);
    }
}
