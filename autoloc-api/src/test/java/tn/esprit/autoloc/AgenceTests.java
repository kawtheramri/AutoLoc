package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

// 5. Interface AgenceRepositoryMock
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {}

// 4. Classe de test
@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    // 6. Méthode addAgence
    private void addAgence(CrudRepository<Agence, Long> repository) {
        int id = (int) System.currentTimeMillis();

        // 7. Création des véhicules
        Vehicule v1 = Vehicule.builder()
                .categorie(CategorieVehicule.SUV)
                .immatriculation("785414TU96" + id)
                .marque("Isuzu")
                .modele("DMax")
                .statut(StatutVehicule.EN_MAINTENANCE)
                .tarifJournalier(new BigDecimal("100"))
                .build();

        Vehicule v2 = Vehicule.builder()
                .categorie(CategorieVehicule.UTILITAIRE)
                .immatriculation("785414TU95" + id)
                .marque("Toyota")
                .modele("Yaris")
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal("80"))
                .build();

        // 7. Création de l'agence
        Agence agence = Agence.builder()
                .nom("Agence ariana")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
                .vehicules(Set.of(v1, v2))
                .build();

        v1.setAgence(agence);
        v2.setAgence(agence);

        // 8. Persister les données
        repository.save(agence);
    }

    // 7. Méthodes de test d'ajout
    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    // Méthode loadAgence
    private void loadAgence(CrudRepository<Agence, Long> repository, String typeDepot) {
        System.out.println("==================================================");
        System.out.println("Type de dépôt utilisé : " + typeDepot);
        System.out.println("Classe réelle du dépôt : " + repository.getClass().getName());

        Agence agence = repository.findById(1L).orElse(null);
        if (agence == null && repository.findAll().iterator().hasNext()) {
            agence = repository.findAll().iterator().next();
        }

        if (agence != null) {
            System.out.println("Agence chargée (ID: " + agence.getIdAgence() + ") : " + agence.getNom() + ", Ville : " + agence.getVille());
            System.out.println("Nombre de véhicules associés : " + agence.getVehicules().size());
            agence.getVehicules().forEach(v ->
                System.out.println("  -> Véhicule : " + v.getMarque() + " " + v.getModele() + " [" + v.getImmatriculation() + "]")
            );
        } else {
            System.out.println("Aucune agence trouvée dans la base de données.");
        }
        System.out.println("==================================================");
    }

    private void loadAgence(CrudRepository<Agence, Long> repository) {
        String typeDepot = (repository instanceof IAgenceRepository)
                ? "fullAgenceRepository (IAgenceRepository / JpaRepository)"
                : "basicAgenceRepository (AgenceRepositoryMock / CrudRepository)";
        loadAgence(repository, typeDepot);
    }

    // Méthodes de test de chargement
    @Test
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "basicAgenceRepository (CrudRepository)");
    }

    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "fullAgenceRepository (IAgenceRepository)");
    }

    // 11. Méthode loadSortedAgences
    @Test
    public void loadSortedAgences() {
        // 12. Charger toutes les agences triées par id décroissants
        List<Agence> agences = fullAgenceRepository.findAll(Sort.by(Sort.Direction.DESC, "idAgence"));

        // 13. Afficher les informations sur les agences (sans les détails sur les véhicules)
        System.out.println("=== Liste des agences triées par ID décroissant ===");
        agences.forEach(agence -> {
            System.out.println("ID: " + agence.getIdAgence()
                    + " | Nom: " + agence.getNom()
                    + " | Ville: " + agence.getVille()
                    + " | Adresse: " + agence.getAdresse()
                    + " | Téléphone: " + agence.getTelephone());
        });
        System.out.println("==================================================");
    }

    // 15. Méthode loadPagedAgences
    @Test
    public void loadPagedAgences() {
        // 16. Pagination par lot de 2, triée par id décroissants
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "idAgence"));
        Page<Agence> agencePage = fullAgenceRepository.findAll(pageable);

        // 17. Afficher le nombre total de pages, la page en cours et les informations sur les agences de cette page
        System.out.println("=== Pagination des agences ===");
        System.out.println("Nombre total de pages : " + agencePage.getTotalPages());
        System.out.println("Page en cours : " + agencePage.getNumber());
        System.out.println("Nombre total d'éléments : " + agencePage.getTotalElements());

        // Code dupliqué entre loadSortedAgences et loadPagedAgences (comme spécifié)
        agencePage.getContent().forEach(agence -> {
            System.out.println("ID: " + agence.getIdAgence()
                    + " | Nom: " + agence.getNom()
                    + " | Ville: " + agence.getVille()
                    + " | Adresse: " + agence.getAdresse()
                    + " | Téléphone: " + agence.getTelephone());
        });
        System.out.println("==============================");
    }
}
