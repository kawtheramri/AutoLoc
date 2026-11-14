package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.calcul.LongueDureeStrategie;
import tn.esprit.autoloc.calcul.StandardStrategie;
import tn.esprit.autoloc.calcul.StrategieOffres;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.calcul.WeekendStrategie;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.AgenceServiceImpl;
import tn.esprit.autoloc.service.VehiculeServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulationTests {

    private VehiculeServiceImpl vehiculeService() {
        return new VehiculeServiceImpl(null, new StrategieOffres(List.of(
                new StandardStrategie(),
                new WeekendStrategie(),
                new LongueDureeStrategie())));
    }

    private Agence agenceDeTest() {
        Vehicule v1 = Vehicule.builder()
                .immatriculation("785414TU")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal("100"))
                .build();
        Vehicule v2 = Vehicule.builder()
                .immatriculation("785415TU")
                .marque("Isuzu")
                .modele("DMax")
                .categorie(CategorieVehicule.SUV)
                .statut(StatutVehicule.DISPONIBLE)
                .tarifJournalier(new BigDecimal("80"))
                .build();

        return Agence.builder()
                .idAgence(11L)
                .nom("Agence ariana")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .ville("Tunis")
                .vehicules(Set.of(v1, v2))
                .build();
    }

    private double totalDe(String rapport) {
        int index = rapport.indexOf("Total tarif : ") + "Total tarif : ".length();
        return Double.parseDouble(rapport.substring(index).trim());
    }

    @Test
    public void simulationStandard3Jours() {
        AgenceServiceImpl service = new AgenceServiceImpl(null, vehiculeService());
        String rapport = service.simuler(agenceDeTest(), 3, VehiculeOffre.STANDARD);

        assertTrue(rapport.startsWith("Id : 11, Nom : Agence ariana, Tel : 71585874\n"));
        assertEquals(540.0, totalDe(rapport), 0.01);
    }

    @Test
    public void simulationWeekend3Jours() {
        AgenceServiceImpl service = new AgenceServiceImpl(null, vehiculeService());
        String rapport = service.simuler(agenceDeTest(), 3, VehiculeOffre.WEEKEND);

        assertEquals(432.0, totalDe(rapport), 0.01);
    }

    @Test
    public void simulationLongueDuree30Jours() {
        AgenceServiceImpl service = new AgenceServiceImpl(null, vehiculeService());
        String rapport = service.simuler(agenceDeTest(), 30, VehiculeOffre.LONGUE_DUREE);

        assertEquals(3937.5, totalDe(rapport), 0.01);
    }
}
