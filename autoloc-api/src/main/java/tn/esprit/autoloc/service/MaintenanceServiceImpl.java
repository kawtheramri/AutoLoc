package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

@Service
public class MaintenanceServiceImpl extends GenericServiceImpl<Maintenance, Long> implements IMaintenanceService {

    public MaintenanceServiceImpl(IMaintenanceRepository maintenanceRepository) {
        super(maintenanceRepository);
    }

    @Override
    protected String getEntityName() {
        return "Maintenance";
    }

    @Override
    protected Long getId(Maintenance maintenance) {
        return maintenance.getIdMaintenance();
    }

    @Override
    protected void setFields(Maintenance existing, Maintenance updated) {
        existing.setDateDebut(updated.getDateDebut());
        existing.setDateFin(updated.getDateFin());
        existing.setDescription(updated.getDescription());
        existing.setVehicule(updated.getVehicule());
    }

    @Override
    public boolean check(Maintenance maintenance, boolean isNew) {
        if (maintenance == null) {
            return false;
        }
        return maintenance.getDateDebut() != null
                && Utils.isNotEmpty(maintenance.getDescription())
                && maintenance.getVehicule() != null;
    }
}
