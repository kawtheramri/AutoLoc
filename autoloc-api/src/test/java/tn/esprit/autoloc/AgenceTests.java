package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {}

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    private void addAgence(CrudRepository<Agence, Long> repository) {
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

        repository.save(agence);
    }

    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
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
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "basicAgenceRepository");
    }

    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "fullAgenceRepository");
    }

    @Test
    public void loadSortedAgences() {
        List<Agence> agences = fullAgenceRepository.findAll(Sort.by("idAgence").descending());
        StringBuilder result = new StringBuilder();

        for (Agence agence : agences) {
            result.append(agence.getIdAgence()).append(" | ").append(agence.getNom()).append("\n");
        }

        fail(result.toString());
    }

    @Test
    public void loadPagedAgences() {
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
}