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
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    private final IEmployeService employeService;

    public EmployeController(@Qualifier("employeServiceImpl") IEmployeService employeService) {
        this.employeService = employeService;
    }

    @GetMapping
    public List<Employe> findAll() {
        return employeService.findAll();
    }

    @GetMapping("/{id}")
    public Employe findById(@PathVariable Long id) {
        return employeService.findById(id);
    }

    // Ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employe create(@Valid @RequestBody Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return employeService.save(employe);
    }

    // Modification
    @PutMapping("/{id}")
    public Employe update(@PathVariable Long id, @Valid @RequestBody Employe employe) {
        employeService.findById(id);          // lève l'exception "introuvable" → 404
        employe.setIdEmploye(id);             // l'id du chemin gagne sur celui du corps
        return employeService.save(employe);
    }

    // Suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeService.deleteById(id);
    }
}
