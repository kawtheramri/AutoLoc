package tn.esprit.autoloc.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.autoloc.domain.Agence;

public class RapportConsole implements IRapport {

    private static final Logger log = LoggerFactory.getLogger(RapportConsole.class);

    private double total;

    @Override
    public void ecrireEntete(Agence agence) {
        String message = "Id : " + agence.getIdAgence()
                + ", Nom : " + agence.getNom()
                + ", Tel : " + agence.getTelephone();
        log.info("{}", message);
    }

    @Override
    public void ajouter(double montant) {
        total += montant;
    }

    @Override
    public double getTotal() {
        return total;
    }
}
