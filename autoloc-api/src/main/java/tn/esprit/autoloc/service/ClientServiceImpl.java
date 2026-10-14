package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client, Long> implements IClientService {

    public ClientServiceImpl(IClientRepository clientRepository) {
        super(clientRepository);
    }

    @Override
    protected String getEntityName() {
        return "Client";
    }

    @Override
    protected Long getId(Client client) {
        return client.getIdClient();
    }

    @Override
    protected void setFields(Client existing, Client updated) {
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setEmail(updated.getEmail());
        existing.setTelephone(updated.getTelephone());
        existing.setNumPermis(updated.getNumPermis());
        existing.setDateInscription(updated.getDateInscription());
    }

    @Override
    public boolean check(Client client, boolean isNew) {
        if (client == null) {
            return false;
        }
        return Utils.isNotEmpty(client.getNom())
                && Utils.isNotEmpty(client.getPrenom())
                && Utils.isNotEmpty(client.getEmail())
                && Utils.isNotEmpty(client.getTelephone())
                && Utils.isNotEmpty(client.getNumPermis());
    }
}
