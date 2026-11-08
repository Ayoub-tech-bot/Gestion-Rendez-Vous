package ma.ensa.gestion.rdv.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ma.ensa.gestion.rdv.model.RendezVous;
import ma.ensa.gestion.rdv.model.StatutRendezVous;

public interface RendezVousService {

    RendezVous prendreRendezVous(RendezVous rdv);

    RendezVous annulerRendezVous(Long rdvId, Long patientId);

    RendezVous reporterRendezVous(Long rdvId, Long patientId, LocalDate nouvelleDate);

    RendezVous confirmerRendezVous(Long rdvId, Long medecinId);

    RendezVous refuserRendezVous(Long rdvId, Long medecinId);

    RendezVous terminerRendezVous(Long rdvId, Long medecinId);

    Optional<RendezVous> trouverParId(Long id);

    List<RendezVous> listerParPatient(Long patientId);

    List<RendezVous> listerParMedecin(Long medecinId);

    List<RendezVous> listerParMedecinEtDate(Long medecinId, LocalDate date);

    List<RendezVous> listerParMedecinEtStatut(Long medecinId, StatutRendezVous statut);

    List<RendezVous> listerAvecFiltres(Long medecinId, LocalDate date, StatutRendezVous statut);
}