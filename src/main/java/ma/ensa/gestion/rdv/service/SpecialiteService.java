package ma.ensa.gestion.rdv.service;

import java.util.List;
import java.util.Optional;

import ma.ensa.gestion.rdv.model.Specialite;

public interface SpecialiteService {

    Specialite creer(Specialite specialite);

    Specialite modifier(Specialite specialite);

    void supprimer(Long id);

    Optional<Specialite> trouverParId(Long id);

    Optional<Specialite> trouverParNom(String nom);

    List<Specialite> listerTout();
}