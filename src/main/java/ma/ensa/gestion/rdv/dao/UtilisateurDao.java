package ma.ensa.gestion.rdv.dao;

import ma.ensa.gestion.rdv.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurDao extends GenericDao<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
}
