package ma.ensa.gestion.rdv.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ma.ensa.gestion.rdv.dao.RendezVousDao;
import ma.ensa.gestion.rdv.dao.impl.RendezVousDaoImpl;
import ma.ensa.gestion.rdv.model.RendezVous;
import ma.ensa.gestion.rdv.model.StatutRendezVous;
import ma.ensa.gestion.rdv.service.RendezVousService;

public class RendezVousServiceImpl implements RendezVousService {

    private static final LocalTime HEURE_OUVERTURE = LocalTime.of(8, 0);
    private static final LocalTime HEURE_FERMETURE = LocalTime.of(18, 0);
    private static final int DUREE_CRENEAU_MINUTES = 30;

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
        validerHeureRendezVous(rdv.getDate(), rdv.getHeureDebut());
        rdv.setHeureFin(rdv.getHeureDebut().plusMinutes(DUREE_CRENEAU_MINUTES));

        // Règle 1: Un médecin n'a pas deux rendez-vous actifs au même créneau
        List<RendezVous> medecinActifs = rendezVousDao.findActiveByMedecinAndDate(
                rdv.getMedecin().getId(), rdv.getDate());
        boolean creneauPris = medecinActifs.stream()
                .anyMatch(autre -> autre.getHeureDebut() != null && autre.getHeureDebut().equals(rdv.getHeureDebut()));
        if (creneauPris) {
            throw new IllegalStateException("Ce créneau horaire est déjà réservé pour ce médecin.");
        }

        // Scénario S5: Vérifier si le patient a déjà un RDV au même créneau
        List<RendezVous> patientActifs = rendezVousDao.findActiveByPatientAndDate(
                rdv.getPatient().getId(), rdv.getDate());
        boolean patientOccupe = patientActifs.stream()
                .anyMatch(autre -> autre.getHeureDebut() != null && autre.getHeureDebut().equals(rdv.getHeureDebut()));
        if (patientOccupe) {
            throw new IllegalStateException("Vous avez déjà un rendez-vous prévu à ce même créneau horaire.");
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
    public RendezVous reporterRendezVous(Long rdvId, Long patientId, LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        RendezVous rdv = getRendezVousDuPatient(rdvId, patientId);

        if (rdv.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Impossible de reporter un rendez-vous passé.");
        }
        if (rdv.getStatus() != StatutRendezVous.EN_ATTENTE
                && rdv.getStatus() != StatutRendezVous.CONFIRME) {
            throw new IllegalStateException("Ce rendez-vous ne peut plus être reporté.");
        }

        validerDateRendezVous(nouvelleDate);
        validerHeureRendezVous(nouvelleDate, nouvelleHeure);

        // Vérifier si le médecin est libre au nouveau créneau
        List<RendezVous> medecinActifs = rendezVousDao.findActiveByMedecinAndDate(
                rdv.getMedecin().getId(), nouvelleDate);
        boolean prisParAutre = medecinActifs.stream()
                .anyMatch(autre -> autre.getId() != rdv.getId()
                        && autre.getHeureDebut() != null
                        && autre.getHeureDebut().equals(nouvelleHeure));
        if (prisParAutre) {
            throw new IllegalStateException("Le nouveau créneau choisi est déjà occupé pour ce médecin.");
        }

        // Vérifier si le patient est libre au nouveau créneau
        List<RendezVous> patientActifs = rendezVousDao.findActiveByPatientAndDate(
                patientId, nouvelleDate);
        boolean patientOccupe = patientActifs.stream()
                .anyMatch(autre -> autre.getId() != rdv.getId()
                        && autre.getHeureDebut() != null
                        && autre.getHeureDebut().equals(nouvelleHeure));
        if (patientOccupe) {
            throw new IllegalStateException("Vous avez déjà un autre rendez-vous prévu à ce même créneau horaire.");
        }

        rdv.setDate(nouvelleDate);
        rdv.setHeureDebut(nouvelleHeure);
        rdv.setHeureFin(nouvelleHeure.plusMinutes(DUREE_CRENEAU_MINUTES));
        rdv.setStatus(StatutRendezVous.EN_ATTENTE);
        return rendezVousDao.update(rdv);
    }

    @Override
    public RendezVous reporterRendezVous(Long rdvId, Long patientId, LocalDate nouvelleDate) {
        RendezVous rdv = getRendezVousDuPatient(rdvId, patientId);
        LocalTime heure = rdv.getHeureDebut() != null ? rdv.getHeureDebut() : HEURE_OUVERTURE;
        return reporterRendezVous(rdvId, patientId, nouvelleDate, heure);
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

    @Override
    public List<LocalTime> getCreneauxDisponibles(Long medecinId, LocalDate date) {
        if (medecinId == null || date == null) {
            return List.of();
        }
        if (date.isBefore(LocalDate.now())) {
            return List.of();
        }
        DayOfWeek jour = date.getDayOfWeek();
        if (jour == DayOfWeek.SATURDAY || jour == DayOfWeek.SUNDAY) {
            return List.of();
        }

        // Récupérer les créneaux déjà occupés pour ce médecin à cette date
        List<RendezVous> actifs = rendezVousDao.findActiveByMedecinAndDate(medecinId, date);
        Set<LocalTime> heuresPrises = actifs.stream()
                .map(RendezVous::getHeureDebut)
                .filter(h -> h != null)
                .collect(Collectors.toSet());

        List<LocalTime> disponibles = new ArrayList<>();
        LocalTime slot = HEURE_OUVERTURE;
        while (slot.plusMinutes(DUREE_CRENEAU_MINUTES).isBefore(HEURE_FERMETURE)
                || slot.plusMinutes(DUREE_CRENEAU_MINUTES).equals(HEURE_FERMETURE)) {
            boolean estPasse = date.equals(LocalDate.now()) && slot.isBefore(LocalTime.now());
            if (!heuresPrises.contains(slot) && !estPasse) {
                disponibles.add(slot);
            }
            slot = slot.plusMinutes(DUREE_CRENEAU_MINUTES);
        }
        return disponibles;
    }

    private void validerHeureRendezVous(LocalDate date, LocalTime heureDebut) {
        if (heureDebut == null) {
            throw new IllegalArgumentException("L'heure du rendez-vous est obligatoire.");
        }
        if (heureDebut.isBefore(HEURE_OUVERTURE) || heureDebut.plusMinutes(DUREE_CRENEAU_MINUTES).isAfter(HEURE_FERMETURE)) {
            throw new IllegalArgumentException("Les créneaux de consultation sont disponibles uniquement de 08h00 à 18h00.");
        }
        if (heureDebut.getMinute() != 0 && heureDebut.getMinute() != 30) {
            throw new IllegalArgumentException("Les rendez-vous doivent débuter sur un créneau de 30 minutes (ex: 08h00, 08h30).");
        }
        if (date.equals(LocalDate.now()) && heureDebut.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Impossible de réserver un créneau horaire déjà passé aujourd'hui.");
        }
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