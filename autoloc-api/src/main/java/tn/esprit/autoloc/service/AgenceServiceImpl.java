package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService {

    public AgenceServiceImpl(IAgenceRepository agenceRepository) {
        super(agenceRepository);
    }

    @Override
    protected String getEntityName() {
        return "Agence";
    }

    @Override
    protected Long getId(Agence agence) {
        return agence.getIdAgence();
    }

    @Override
    protected void setFields(Agence existing, Agence updated) {
        existing.setNom(updated.getNom());
        existing.setAdresse(updated.getAdresse());
        existing.setTelephone(updated.getTelephone());
        existing.setVille(updated.getVille());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        if (agence == null) {
            return false;
        }
        return Utils.isNotEmpty(agence.getNom())
                && Utils.isNotEmpty(agence.getAdresse())
                && Utils.isNotEmpty(agence.getTelephone())
                && Utils.isNotEmpty(agence.getVille());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }
}
