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
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    public MaintenanceController(@Qualifier("maintenanceServiceImpl") IMaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @GetMapping("/{id}")
    public Maintenance findById(@PathVariable Long id) {
        return maintenanceService.findById(id);
    }

    // Ajout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Maintenance create(@Valid @RequestBody Maintenance maintenance) {
        if (maintenance.getIdMaintenance() != null) {
            throw new IllegalArgumentException("L'identifiant doit être vide à la création");
        }
        return maintenanceService.save(maintenance);
    }

    // Modification
    @PutMapping("/{id}")
    public Maintenance update(@PathVariable Long id, @Valid @RequestBody Maintenance maintenance) {
        maintenanceService.findById(id);         // lève l'exception "introuvable" → 404
        maintenance.setIdMaintenance(id);      // l'id du chemin gagne sur celui du corps
        return maintenanceService.save(maintenance);
    }

    // Suppression
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        maintenanceService.deleteById(id);
    }
}
