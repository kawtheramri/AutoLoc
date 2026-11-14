package tn.esprit.autoloc.io;

import lombok.Delegate;
import tn.esprit.autoloc.domain.Agence;

public class RapportTexte implements IRapport {

    @Delegate                      // pattern Decorator : délègue les méthodes non redéfinies
    private final IRapport rapport;

    private final StringBuilder lignes = new StringBuilder();

    public RapportTexte(IRapport rapport) {
        this.rapport = rapport;
    }

    @Override
    public void ecrireEntete(Agence agence) {
        lignes.append("Id : " + agence.getIdAgence()
                + ", Nom : " + agence.getNom()
                + ", Tel : " + agence.getTelephone() + "\n");
        rapport.ecrireEntete(agence);   // l'entête est aussi affichée sur la console
    }

    public void ecrireTotal(double total) {
        lignes.append("Total tarif : " + total);
    }

    public String contenu() {
        return lignes.toString();
    }
}
