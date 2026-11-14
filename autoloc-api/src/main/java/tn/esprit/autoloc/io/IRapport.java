package tn.esprit.autoloc.io;

import tn.esprit.autoloc.domain.Agence;

public interface IRapport {

    void ecrireEntete(Agence agence);   // affiche l'entête sur la console

    void ajouter(double montant);        // ajoute un tarif au total

    double getTotal();                   // retourne le total
}
