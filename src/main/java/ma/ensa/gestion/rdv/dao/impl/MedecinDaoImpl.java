package ma.ensa.gestion.rdv.dao.impl;

import ma.ensa.gestion.rdv.dao.MedecinDao;
import ma.ensa.gestion.rdv.model.Medecin;
import ma.ensa.gestion.rdv.model.StatutRendezVous;
import ma.ensa.gestion.rdv.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MedecinDaoImpl extends GenericDaoImpl<Medecin, Long> implements MedecinDao {

    public MedecinDaoImpl() {
        super(Medecin.class);
    }

    @Override
    public List<Medecin> findBySpecialite(Long specialiteId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Medecin m WHERE m.specialite.id = :specialiteId";
            return session.createQuery(hql, Medecin.class)
                    .setParameter("specialiteId", specialiteId)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des médecins par spécialité: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasActiveRendezVous(Long medecinId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(r) FROM RendezVous r " +
                         "WHERE r.medecin.id = :medecinId " +
                         "AND r.status IN (:enAttente, :confirme)";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("medecinId", medecinId)
                    .setParameter("enAttente", StatutRendezVous.EN_ATTENTE)
                    .setParameter("confirme", StatutRendezVous.CONFIRME)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification des rendez-vous actifs du médecin: " + e.getMessage(), e);
        }
    }
}
