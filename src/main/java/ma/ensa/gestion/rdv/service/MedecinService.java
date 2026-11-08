package ma.ensa.gestion.rdv.service;

import java.util.List;
import java.util.Optional;

import ma.ensa.gestion.rdv.model.Medecin;

public interface MedecinService {

    Medecin creer(Medecin medecin);

    Medecin modifier(Medecin medecin);

    void supprimer(Long id);

    Optional<Medecin> trouverParId(Long id);

    List<Medecin> listerTout();

    List<Medecin> listerParSpecialite(Long specialiteId);
}