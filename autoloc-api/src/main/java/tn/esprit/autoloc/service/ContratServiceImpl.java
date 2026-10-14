package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

@Service
public class ContratServiceImpl extends GenericServiceImpl<Contrat, Long> implements IContratService {

    public ContratServiceImpl(IContratRepository contratRepository) {
        super(contratRepository);
    }

    @Override
    protected String getEntityName() {
        return "Contrat";
    }

    @Override
    protected Long getId(Contrat contrat) {
        return contrat.getIdContrat();
    }

    @Override
    protected void setFields(Contrat existing, Contrat updated) {
        existing.setDateSignature(updated.getDateSignature());
        existing.setMontantTotal(updated.getMontantTotal());
        existing.setValide(updated.getValide());
    }

    @Override
    public boolean check(Contrat contrat, boolean isNew) {
        if (contrat == null) {
            return false;
        }
        return contrat.getDateSignature() != null
                && contrat.getMontantTotal() != null
                && contrat.getValide() != null;
    }
}
