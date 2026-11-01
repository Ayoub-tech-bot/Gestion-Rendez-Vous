package ma.ensa.gestion.rdv.dao;

import ma.ensa.gestion.rdv.model.Medecin;

import java.util.List;

public interface MedecinDao extends GenericDao<Medecin, Long> {
    List<Medecin> findBySpecialite(Long specialiteId);
    boolean hasActiveRendezVous(Long medecinId);
}
