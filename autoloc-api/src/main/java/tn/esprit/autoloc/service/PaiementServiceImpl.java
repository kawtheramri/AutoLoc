package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;

@Service
public class PaiementServiceImpl extends GenericServiceImpl<Paiement, Long> implements IPaiementService {

    public PaiementServiceImpl(IPaiementRepository paiementRepository) {
        super(paiementRepository);
    }

    @Override
    protected String getEntityName() {
        return "Paiement";
    }

    @Override
    protected Long getId(Paiement paiement) {
        return paiement.getIdPaiement();
    }

    @Override
    protected void setFields(Paiement existing, Paiement updated) {
        throw new UnsupportedOperationException("Le paiement est en lecture seule");
    }

    @Override
    public boolean check(Paiement paiement, boolean isNew) {
        return paiement != null;
    }
}
