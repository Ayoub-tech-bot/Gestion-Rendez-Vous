package ma.ensa.gestion.rdv.dao;

import ma.ensa.gestion.rdv.model.RendezVous;
import ma.ensa.gestion.rdv.model.StatutRendezVous;

import java.time.LocalDate;
import java.util.List;

public interface RendezVousDao extends GenericDao<RendezVous, Long> {

    List<RendezVous> findByPatient(Long patientId);

    List<RendezVous> findByMedecin(Long medecinId);

    List<RendezVous> findByMedecinAndDate(Long medecinId, LocalDate date);

    List<RendezVous> findActiveByMedecinAndDate(Long medecinId, LocalDate date);

    List<RendezVous> findActiveByPatientAndDate(Long patientId, LocalDate date);

    List<RendezVous> findByMedecinAndStatut(Long medecinId, StatutRendezVous statut);

    List<RendezVous> findAllWithFilters(Long medecinId, LocalDate date, StatutRendezVous statut);
}
