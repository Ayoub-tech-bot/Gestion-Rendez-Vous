package ma.ensa.gestion.rdv.service.impl;

import java.util.List;
import java.util.Optional;

import ma.ensa.gestion.rdv.dao.SpecialiteDao;
import ma.ensa.gestion.rdv.dao.impl.SpecialiteDaoImpl;
import ma.ensa.gestion.rdv.model.Specialite;
import ma.ensa.gestion.rdv.service.SpecialiteService;

public class SpecialiteServiceImpl implements SpecialiteService {

    private final SpecialiteDao specialiteDao;

    public SpecialiteServiceImpl() {
        this.specialiteDao = new SpecialiteDaoImpl();
    }

    @Override
    public Specialite creer(Specialite specialite) {
        if (specialite == null || specialite.getNom() == null || specialite.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de la spécialité est obligatoire.");
        }

        String nomNettoye = specialite.getNom().trim();
        if (specialiteDao.findByNom(nomNettoye).isPresent()) {
            throw new IllegalArgumentException("Une spécialité avec ce nom existe déjà.");
        }

        specialite.setNom(nomNettoye);
        return specialiteDao.save(specialite);
    }

    @Override
    public Specialite modifier(Specialite specialite) {
        if (specialite == null || specialite.getId() == 0) {
            throw new IllegalArgumentException("Spécialité invalide.");
        }
        if (specialite.getNom() == null || specialite.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de la spécialité est obligatoire.");
        }

        String nomNettoye = specialite.getNom().trim();
        Optional<Specialite> existante = specialiteDao.findByNom(nomNettoye);

        if (existante.isPresent() && existante.get().getId() != specialite.getId()) {
            throw new IllegalArgumentException("Une autre spécialité porte déjà ce nom.");
        }

        specialite.setNom(nomNettoye);
        return specialiteDao.update(specialite);
    }

    @Override
    public void supprimer(Long id) {
        if (specialiteDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Spécialité introuvable.");
        }
        
        if (specialiteDao.hasMedecins(id)) {
            throw new IllegalStateException("Impossible de supprimer cette spécialité car des médecins y sont rattachés.");
        }

        specialiteDao.delete(id);
    }

    @Override
    public Optional<Specialite> trouverParId(Long id) {
        return specialiteDao.findById(id);
    }

    @Override
    public Optional<Specialite> trouverParNom(String nom) {
        return specialiteDao.findByNom(nom);
    }

    @Override
    public List<Specialite> listerTout() {
        return specialiteDao.findAll();
    }
}