package tn.esprit.autoloc.io;

public class RapportFactory {

    private RapportFactory() {
    }

    public static RapportTexte creerRapport() {
        return new RapportTexte(new RapportConsole());
    }
}
