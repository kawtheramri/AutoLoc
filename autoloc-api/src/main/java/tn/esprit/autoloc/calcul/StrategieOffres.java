package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class StrategieOffres {

    private final Map<VehiculeOffre, IOffreStrategie> strategies = new EnumMap<>(VehiculeOffre.class);

    public StrategieOffres(List<IOffreStrategie> strategiesDisponibles) {
        for (IOffreStrategie strategie : strategiesDisponibles) {
            strategies.put(strategie.getOffre(), strategie);
        }
    }

    public IOffreStrategie get(VehiculeOffre offre) {
        IOffreStrategie strategie = strategies.get(offre);
        if (strategie == null) {
            throw new IllegalArgumentException("Offre non supportée : " + offre);
        }
        return strategie;
    }
}
