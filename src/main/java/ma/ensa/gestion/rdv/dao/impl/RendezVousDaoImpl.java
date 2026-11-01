package ma.ensa.gestion.rdv.dao.impl;

import ma.ensa.gestion.rdv.dao.RendezVousDao;
import ma.ensa.gestion.rdv.model.RendezVous;
import ma.ensa.gestion.rdv.model.StatutRendezVous;
import ma.ensa.gestion.rdv.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class RendezVousDaoImpl extends GenericDaoImpl<RendezVous, Long> implements RendezVousDao {

    public RendezVousDaoImpl() {
        super(RendezVous.class);
    }

    @Override
    public List<RendezVous> findByPatient(Long patientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM RendezVous r WHERE r.patient.id = :patientId ORDER BY r.date DESC";
            return session.createQuery(hql, RendezVous.class)
                    .setParameter("patientId", patientId)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous du patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> findByMedecin(Long medecinId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM RendezVous r WHERE r.medecin.id = :medecinId ORDER BY r.date DESC";
            return session.createQuery(hql, RendezVous.class)
                    .setParameter("medecinId", medecinId)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous du médecin: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> findByMedecinAndDate(Long medecinId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.date = :date";
            return session.createQuery(hql, RendezVous.class)
                    .setParameter("medecinId", medecinId)
                    .setParameter("date", date)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous par date: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> findActiveByMedecinAndDate(Long medecinId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM RendezVous r " +
                         "WHERE r.medecin.id = :medecinId " +
                         "AND r.date = :date " +
                         "AND r.status IN (:enAttente, :confirme)";
            return session.createQuery(hql, RendezVous.class)
                    .setParameter("medecinId", medecinId)
                    .setParameter("date", date)
                    .setParameter("enAttente", StatutRendezVous.EN_ATTENTE)
                    .setParameter("confirme", StatutRendezVous.CONFIRME)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous actifs: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> findByMedecinAndStatut(Long medecinId, StatutRendezVous statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.status = :statut";
            return session.createQuery(hql, RendezVous.class)
                    .setParameter("medecinId", medecinId)
                    .setParameter("statut", statut)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous par statut: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> findAllWithFilters(Long medecinId, LocalDate date, StatutRendezVous statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM RendezVous r WHERE 1=1");
            if (medecinId != null) {
                hql.append(" AND r.medecin.id = :medecinId");
            }
            if (date != null) {
                hql.append(" AND r.date = :date");
            }
            if (statut != null) {
                hql.append(" AND r.status = :statut");
            }
            hql.append(" ORDER BY r.date DESC");

            Query<RendezVous> query = session.createQuery(hql.toString(), RendezVous.class);
            if (medecinId != null) {
                query.setParameter("medecinId", medecinId);
            }
            if (date != null) {
                query.setParameter("date", date);
            }
            if (statut != null) {
                query.setParameter("statut", statut);
            }
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche filtrée des rendez-vous: " + e.getMessage(), e);
        }
    }
}
