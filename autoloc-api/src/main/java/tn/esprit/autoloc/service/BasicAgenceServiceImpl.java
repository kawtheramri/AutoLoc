package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

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
    public Agence create(Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("L'identifiant de l'agence doit être vide à la création");
        }
        if (!check(agence, true)) {
            throw new IllegalArgumentException("La vérification de l'agence a échoué");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", String.valueOf(id)));
    }

    @Override
    public List<Agence> findAll() {
        return null;
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = findById(id);
        if (!check(agence, false)) {
            throw new IllegalArgumentException("La vérification de l'agence a échoué");
        }
        existing.setNom(agence.getNom());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        existing.setVille(agence.getVille());
        return agenceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", String.valueOf(id));
        }
        agenceRepository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }
}
