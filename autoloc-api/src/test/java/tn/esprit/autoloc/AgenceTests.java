package tn.esprit.autoloc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IAgenceService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Repository
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {}

@SpringBootTest
@Disabled("Tests des anciens ateliers")
class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    @Qualifier("agenceServiceImpl")
    private IAgenceService agenceService;

    private Agence addAgence(CrudRepository<Agence, Long> repository) {
        int timestamp = (int) System.currentTimeMillis();

        Vehicule v1 = Vehicule.builder()
                .immatriculation("785414TU" + timestamp)
                .marque("Isuzu")
                .modele("DMax")
                .statut(StatutVehicule.EN_MAINTENANCE)
                .tarifJournalier(new BigDecimal("100"))
                .build();
        v1.setCategorie(CategorieVehicule.SUV);

        Vehicule v2 = Vehicule.builder()
                .immatriculation("785415TU" + timestamp)
                .marque("Toyota")
                .modele("Yaris")
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal("80"))
                .build();
        v2.setCategorie(CategorieVehicule.UTILITAIRE);

        Agence agence = Agence.builder()
                .nom("Agence ariana")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
                .vehicules(Set.of(v1, v2))
                .build();

        v1.setAgence(agence);
        v2.setAgence(agence);

        return repository.save(agence);
    }

    @Test
    void createAgence() {
        int timestamp = (int) System.currentTimeMillis();

        Agence agence = Agence.builder()
                .nom("Agence service " + timestamp)
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
                .build();

        Agence created = agenceService.create(agence);

        fail("Agence créée : " + created.getIdAgence() + " | " + created.getNom()
                + " | " + created.getVille());
    }

    @Test
    void basicAddAgence() {
        Agence saved = addAgence(basicAgenceRepository);

        assertNotNull(saved);
        assertNotNull(saved.getIdAgence(), "L'agence creee doit avoir un identifiant");
    }

    @Test
    void fullAddAgence() {
        Agence saved = addAgence(fullAgenceRepository);

        assertNotNull(saved);
        assertNotNull(saved.getIdAgence(), "L'agence creee doit avoir un identifiant");
        assertEquals(2, saved.getVehicules().size(), "L'agence doit contenir ses 2 vehicules");
    }

    private void loadAgence(CrudRepository<Agence, Long> repository, String repoName) {
        StringBuilder result = new StringBuilder();
        result.append("=== Dépôt : ").append(repoName).append(" ===\n");

        repository.findAll().forEach(agence -> {
            result.append("Agence ")
                    .append(agence.getIdAgence())
                    .append(" : ")
                    .append(agence.getNom())
                    .append(" (")
                    .append(agence.getVehicules().size())
                    .append(" véhicules)\n");

            agence.getVehicules().forEach(vehicule -> result.append("  Véhicule ")
                    .append(vehicule.getIdVehicule())
                    .append(" : ")
                    .append(vehicule.getImmatriculation())
                    .append('\n'));
        });

        fail(result.toString());
    }

    @Test
    void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "basicAgenceRepository");
    }

    @Test
    void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "fullAgenceRepository");
    }

    @Test
    void loadSortedAgences() {
        List<Agence> agences = fullAgenceRepository.findAll(Sort.by("idAgence").descending());
        StringBuilder result = new StringBuilder();

        for (Agence agence : agences) {
            result.append(agence.getIdAgence()).append(" | ").append(agence.getNom()).append("\n");
        }

        fail(result.toString());
    }

    @Test
    void loadPagedAgences() {
        StringBuilder result = new StringBuilder();
        int pageNum = 0;
        int pageSize = 2;
        Page<Agence> page;

        do {
            Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("idAgence").descending());
            page = fullAgenceRepository.findAll(pageable);

            result.append("--- PAGE ").append(page.getNumber() + 1)
                    .append(" / ").append(page.getTotalPages()).append(" ---\n");

            for (Agence agence : page.getContent()) {
                result.append(agence.getIdAgence()).append(" | ").append(agence.getNom()).append("\n");
            }

            result.append("\n");
            pageNum++;

        } while (page.hasNext());

        fail(result.toString());
    }

    // 24. Tests de modification (Deux tests)
    private void updateAgence(CrudRepository<Agence, Long> repository, String repoName) {
        int timestamp = (int) System.currentTimeMillis();

        Agence agence = repository.findAll().iterator().hasNext()
                ? repository.findAll().iterator().next()
                : null;

        if (agence == null) {
            addAgence(repository);
            agence = repository.findAll().iterator().next();
        }

        agence.setNom("Agence modifiée " + timestamp);
        agence.setTelephone("71333333");
        agence.setVille("Ariana");

        Agence updated = repository.save(agence);

        fail("=== Dépôt : " + repoName + " ===\nAgence modifiée : "
                + updated.getIdAgence() + " | " + updated.getNom()
                + " | " + updated.getVille() + " | " + updated.getTelephone());
    }

    @Test
    void basicUpdateAgence() {
        updateAgence(basicAgenceRepository, "basicAgenceRepository");
    }

    @Test
    void fullUpdateAgence() {
        updateAgence(fullAgenceRepository, "fullAgenceRepository");
    }

    // 25. Tests de suppression (Deux tests)
    private void deleteAgence(CrudRepository<Agence, Long> repository, String repoName) {
        addAgence(repository);
        Agence agence = null;
        for (Agence a : repository.findAll()) {
            if (a.getVehicules() != null && !a.getVehicules().isEmpty()) {
                agence = a;
                break;
            }
        }

        if (agence == null) {
            fail("Aucune agence avec véhicules trouvée.");
            return;
        }

        Long id = agence.getIdAgence();
        int nbVehicules = agence.getVehicules().size();

        // Aide : désaffecter le parent des véhicules avant de supprimer l'agence
        if (agence.getVehicules() != null) {
            for (Vehicule vehicule : agence.getVehicules()) {
                vehicule.setAgence(null);
                vehiculeRepository.save(vehicule);
            }
            agence.setVehicules(new HashSet<>());
            repository.save(agence);
        }

        // Suppression de l'agence
        repository.deleteById(id);

        fail("=== Dépôt : " + repoName + " ===\nAgence supprimée : ID "
                + id + " (après désaffectation de " + nbVehicules + " véhicules)");
    }

    @Test
    void basicDeleteAgence() {
        deleteAgence(basicAgenceRepository, "basicAgenceRepository");
    }

    @Test
    void fullDeleteAgence() {
        deleteAgence(fullAgenceRepository, "fullAgenceRepository");
    }
}