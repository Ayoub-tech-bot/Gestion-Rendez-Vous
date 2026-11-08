package ma.ensa.gestion.rdv.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ma.ensa.gestion.rdv.dao.RendezVousDao;
import ma.ensa.gestion.rdv.dao.impl.RendezVousDaoImpl;
import ma.ensa.gestion.rdv.model.RendezVous;
import ma.ensa.gestion.rdv.model.StatutRendezVous;
import ma.ensa.gestion.rdv.service.RendezVousService;

public class RendezVousServiceImpl implements RendezVousService {

    private final RendezVousDao rendezVousDao;

    public RendezVousServiceImpl() {
        this.rendezVousDao = new RendezVousDaoImpl();
    }

    @Override
    public RendezVous prendreRendezVous(RendezVous rdv) {
        if (rdv == null || rdv.getPatient() == null || rdv.getMedecin() == null) {
            throw new IllegalArgumentException("Le patient et le médecin sont obligatoires.");
        }
        validerDateRendezVous(rdv.getDate());

        List<RendezVous> actifs = rendezVousDao.findActiveByMedecinAndDate(
                rdv.getMedecin().getId(), rdv.getDate());
        if (!actifs.isEmpty()) {
            throw new IllegalStateException("Ce créneau est déjà réservé pour ce médecin.");
        }

        rdv.setStatus(StatutRendezVous.EN_ATTENTE);
        return rendezVousDao.save(rdv);
    }

    @Override
    public RendezVous annulerRendezVous(Long rdvId, Long patientId) {
        RendezVous rdv = getRendezVousDuPatient(rdvId, patientId);

        if (rdv.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Impossible d'annuler un rendez-vous passé.");
        }
        if (rdv.getStatus() != StatutRendezVous.EN_ATTENTE
                && rdv.getStatus() != StatutRendezVous.CONFIRME) {
            throw new IllegalStateException("Ce rendez-vous ne peut plus être annulé.");
        }

        rdv.setStatus(StatutRendezVous.ANNULE);
        return rendezVousDao.update(rdv);
    }

    @Override
    public RendezVous reporterRendezVous(Long rdvId, Long patientId, LocalDate nouvelleDate) {
        RendezVous rdv = getRendezVousDuPatient(rdvId, patientId);

        if (rdv.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Impossible de reporter un rendez-vous passé.");
        }
        if (rdv.getStatus() != StatutRendezVous.EN_ATTENTE
                && rdv.getStatus() != StatutRendezVous.CONFIRME) {
            throw new IllegalStateException("Ce rendez-vous ne peut plus être reporté.");
        }

        validerDateRendezVous(nouvelleDate);

        List<RendezVous> actifs = rendezVousDao.findActiveByMedecinAndDate(
                rdv.getMedecin().getId(), nouvelleDate);
        boolean prisParAutre = actifs.stream().anyMatch(autre -> autre.getId() != rdv.getId());
        if (prisParAutre) {
            throw new IllegalStateException("Le nouveau créneau choisi est déjà occupé.");
        }

        rdv.setDate(nouvelleDate);
        rdv.setStatus(StatutRendezVous.EN_ATTENTE);
        return rendezVousDao.update(rdv);
    }

    @Override
    public RendezVous confirmerRendezVous(Long rdvId, Long medecinId) {
        RendezVous rdv = getRendezVousDuMedecin(rdvId, medecinId);

        if (rdv.getStatus() != StatutRendezVous.EN_ATTENTE) {
            throw new IllegalStateException("Seul un rendez-vous en attente peut être confirmé.");
        }

        rdv.setStatus(StatutRendezVous.CONFIRME);
        return rendezVousDao.update(rdv);
    }

    @Override
    public RendezVous refuserRendezVous(Long rdvId, Long medecinId) {
        RendezVous rdv = getRendezVousDuMedecin(rdvId, medecinId);

        if (rdv.getStatus() != StatutRendezVous.EN_ATTENTE) {
            throw new IllegalStateException("Seul un rendez-vous en attente peut être refusé.");
        }

        rdv.setStatus(StatutRendezVous.REFUSE);
        return rendezVousDao.update(rdv);
    }

    @Override
    public RendezVous terminerRendezVous(Long rdvId, Long medecinId) {
        RendezVous rdv = getRendezVousDuMedecin(rdvId, medecinId);

        if (rdv.getStatus() != StatutRendezVous.CONFIRME) {
            throw new IllegalStateException("Seul un rendez-vous confirmé peut être marqué comme terminé.");
        }

        rdv.setStatus(StatutRendezVous.TERMINE);
        return rendezVousDao.update(rdv);
    }

    @Override
    public Optional<RendezVous> trouverParId(Long id) {
        return rendezVousDao.findById(id);
    }

    @Override
    public List<RendezVous> listerParPatient(Long patientId) {
        return rendezVousDao.findByPatient(patientId);
    }

    @Override
    public List<RendezVous> listerParMedecin(Long medecinId) {
        return rendezVousDao.findByMedecin(medecinId);
    }

    @Override
    public List<RendezVous> listerParMedecinEtDate(Long medecinId, LocalDate date) {
        return rendezVousDao.findByMedecinAndDate(medecinId, date);
    }

    @Override
    public List<RendezVous> listerParMedecinEtStatut(Long medecinId, StatutRendezVous statut) {
        return rendezVousDao.findByMedecinAndStatut(medecinId, statut);
    }

    @Override
    public List<RendezVous> listerAvecFiltres(Long medecinId, LocalDate date, StatutRendezVous statut) {
        return rendezVousDao.findAllWithFilters(medecinId, date, statut);
    }

    private void validerDateRendezVous(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La date du rendez-vous est obligatoire.");
        }
        
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Impossible de prendre un rendez-vous dans le passé.");
        }
        
        DayOfWeek jour = date.getDayOfWeek();
        if (jour == DayOfWeek.SATURDAY || jour == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Les rendez-vous ne sont disponibles que du lundi au vendredi.");
        }
    }

    private RendezVous getRendezVousDuPatient(Long rdvId, Long patientId) {
        RendezVous rdv = rendezVousDao.findById(rdvId)
                .orElseThrow(() -> new IllegalArgumentException("Rendez-vous introuvable."));
        
        if (rdv.getPatient() == null || rdv.getPatient().getId() != patientId) {
            throw new IllegalStateException("Action non autorisée : ce rendez-vous ne vous appartient pas.");
        }
        return rdv;
    }

    private RendezVous getRendezVousDuMedecin(Long rdvId, Long medecinId) {
        RendezVous rdv = rendezVousDao.findById(rdvId)
                .orElseThrow(() -> new IllegalArgumentException("Rendez-vous introuvable."));
        
        if (rdv.getMedecin() == null || rdv.getMedecin().getId() != medecinId) {
            throw new IllegalStateException("Action non autorisée : vous n'êtes pas le médecin de ce rendez-vous.");
        }
        return rdv;
    }
}