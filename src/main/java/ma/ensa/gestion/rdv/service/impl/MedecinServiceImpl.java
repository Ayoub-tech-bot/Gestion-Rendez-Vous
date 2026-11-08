package ma.ensa.gestion.rdv.service.impl;

import java.util.List;
import java.util.Optional;

import ma.ensa.gestion.rdv.dao.MedecinDao;
import ma.ensa.gestion.rdv.dao.UtilisateurDao;
import ma.ensa.gestion.rdv.dao.impl.MedecinDaoImpl;
import ma.ensa.gestion.rdv.dao.impl.UtilisateurDaoImpl;
import ma.ensa.gestion.rdv.model.Medecin;
import ma.ensa.gestion.rdv.model.Role;
import ma.ensa.gestion.rdv.model.Utilisateur;
import ma.ensa.gestion.rdv.service.MedecinService;
import ma.ensa.gestion.rdv.util.PasswordUtil;

public class MedecinServiceImpl implements MedecinService {

    private final MedecinDao medecinDao;
    private final UtilisateurDao utilisateurDao;

    public MedecinServiceImpl() {
        this.medecinDao = new MedecinDaoImpl();
        this.utilisateurDao = new UtilisateurDaoImpl();
    }

    @Override
    public Medecin creer(Medecin medecin) {
        if (medecin == null || medecin.getEmail() == null || medecin.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email du médecin est obligatoire.");
        }
        if (medecin.getNom() == null || medecin.getNom().isBlank()
                || medecin.getPrenom() == null || medecin.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires.");
        }
        if (medecin.getMotDePasse() == null || medecin.getMotDePasse().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }
        if (medecin.getSpecialite() == null) {
            throw new IllegalArgumentException("La spécialité du médecin est obligatoire.");
        }

        String emailNettoye = medecin.getEmail().trim();
        if (utilisateurDao.findByEmail(emailNettoye).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé par un autre compte.");
        }

        medecin.setEmail(emailNettoye);
        medecin.setMotDePasse(PasswordUtil.hashPassword(medecin.getMotDePasse()));
        medecin.setRole(Role.MEDICINE);

        return medecinDao.save(medecin);
    }

    @Override
    public Medecin modifier(Medecin medecin) {
        if (medecin == null || medecin.getId() == 0) {
            throw new IllegalArgumentException("Médecin invalide.");
        }

        Medecin existant = medecinDao.findById(medecin.getId())
                .orElseThrow(() -> new IllegalArgumentException("Médecin introuvable."));

        String emailNettoye = medecin.getEmail().trim();
        Optional<Utilisateur> userAvecEmail = utilisateurDao.findByEmail(emailNettoye);
        if (userAvecEmail.isPresent() && userAvecEmail.get().getId() != medecin.getId()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé par un autre compte.");
        }

        existant.setNom(medecin.getNom());
        existant.setPrenom(medecin.getPrenom());
        existant.setEmail(emailNettoye);
        existant.setSpecialite(medecin.getSpecialite());

        if (medecin.getMotDePasse() != null && !medecin.getMotDePasse().isBlank()) {
            existant.setMotDePasse(PasswordUtil.hashPassword(medecin.getMotDePasse()));
        }

        return medecinDao.update(existant);
    }

    @Override
    public void supprimer(Long id) {
        if (medecinDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Médecin introuvable.");
        }
        
        if (medecinDao.hasActiveRendezVous(id)) {
            throw new IllegalStateException("Impossible de supprimer ce médecin car il a des rendez-vous actifs.");
        }

        medecinDao.delete(id);
    }

    @Override
    public Optional<Medecin> trouverParId(Long id) {
        return medecinDao.findById(id);
    }

    @Override
    public List<Medecin> listerTout() {
        return medecinDao.findAll();
    }

    @Override
    public List<Medecin> listerParSpecialite(Long specialiteId) {
        return medecinDao.findBySpecialite(specialiteId);
    }
}