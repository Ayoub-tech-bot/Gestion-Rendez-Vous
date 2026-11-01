package ma.ensa.gestion.rdv.dao;

import ma.ensa.gestion.rdv.model.Specialite;

import java.util.Optional;

public interface SpecialiteDao extends GenericDao<Specialite, Long> {
    Optional<Specialite> findByNom(String nom);
    boolean hasMedecins(Long specialiteId);
}
