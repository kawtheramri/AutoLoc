package tn.esprit.autoloc.io;

import tn.esprit.autoloc.domain.Agence;

public class RapportConsole implements IRapport {

    private double total;

    @Override
    public void ecrireEntete(Agence agence) {
        System.out.println("Id : " + agence.getIdAgence()
                + ", Nom : " + agence.getNom()
                + ", Tel : " + agence.getTelephone());
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
