package ma.ensa.gestion.rdv.service.impl;

import java.util.Optional;

import ma.ensa.gestion.rdv.dao.PatientDao;
import ma.ensa.gestion.rdv.dao.UtilisateurDao;
import ma.ensa.gestion.rdv.dao.impl.PatientDaoImpl;
import ma.ensa.gestion.rdv.dao.impl.UtilisateurDaoImpl;
import ma.ensa.gestion.rdv.model.Patient;
import ma.ensa.gestion.rdv.model.Role;
import ma.ensa.gestion.rdv.model.Utilisateur;
import ma.ensa.gestion.rdv.service.UtilisateurService;
import ma.ensa.gestion.rdv.util.PasswordUtil;

public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurDao utilisateurDao;
    private final PatientDao patientDao;

    public UtilisateurServiceImpl() {
        this.utilisateurDao = new UtilisateurDaoImpl();
        this.patientDao = new PatientDaoImpl();
    }

    @Override
    public Patient inscrirePatient(Patient patient) {
        if (patient == null || patient.getEmail() == null || patient.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        if (patient.getMotDePasse() == null || patient.getMotDePasse().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }
        if (emailExiste(patient.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        patient.setMotDePasse(PasswordUtil.hashPassword(patient.getMotDePasse()));
        patient.setRole(Role.PATIENT);

        return patientDao.save(patient);
    }

    @Override
    public Optional<Utilisateur> authentifier(String email, String motDePasse) {
        if (email == null || motDePasse == null) {
            return Optional.empty();
        }
        Optional<Utilisateur> userOpt = utilisateurDao.findByEmail(email.trim());
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (PasswordUtil.checkPassword(motDePasse, user.getMotDePasse())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilisateur> trouverParId(Long id) {
        return utilisateurDao.findById(id);
    }

    @Override
    public Optional<Utilisateur> trouverParEmail(String email) {
        return utilisateurDao.findByEmail(email);
    }

    @Override
    public boolean emailExiste(String email) {
        return utilisateurDao.findByEmail(email.trim()).isPresent();
    }
}