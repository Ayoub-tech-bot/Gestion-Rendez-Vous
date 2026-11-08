package ma.ensa.gestion.rdv.service;

import java.util.Optional;

import ma.ensa.gestion.rdv.model.Patient;
import ma.ensa.gestion.rdv.model.Utilisateur;

public interface UtilisateurService {

    Patient inscrirePatient(Patient patient);

    Optional<Utilisateur> authentifier(String email, String motDePasse);

    Optional<Utilisateur> trouverParId(Long id);

    Optional<Utilisateur> trouverParEmail(String email);

    boolean emailExiste(String email);
}