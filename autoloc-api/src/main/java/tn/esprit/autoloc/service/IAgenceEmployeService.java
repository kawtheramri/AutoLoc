package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Agence;

public interface IAgenceEmployeService {

    Agence ajouterEmployes(Long idAgence, Agence donnees);
}
