package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

@Service
public class EmployeServiceImpl extends GenericServiceImpl<Employe, Long> implements IEmployeService {

    public EmployeServiceImpl(IEmployeRepository employeRepository) {
        super(employeRepository);
    }

    @Override
    protected String getEntityName() {
        return "Employe";
    }

    @Override
    protected Long getId(Employe employe) {
        return employe.getIdEmploye();
    }

    @Override
    protected void setFields(Employe existing, Employe updated) {
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setRole(updated.getRole());
        existing.setAgence(updated.getAgence());
    }

    @Override
    public boolean check(Employe employe, boolean isNew) {
        if (employe == null) {
            return false;
        }
        return Utils.isNotEmpty(employe.getNom())
                && Utils.isNotEmpty(employe.getPrenom())
                && employe.getRole() != null;
    }
}
