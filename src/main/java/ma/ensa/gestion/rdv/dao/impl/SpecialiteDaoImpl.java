package ma.ensa.gestion.rdv.dao.impl;

import ma.ensa.gestion.rdv.dao.SpecialiteDao;
import ma.ensa.gestion.rdv.model.Specialite;
import ma.ensa.gestion.rdv.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class SpecialiteDaoImpl extends GenericDaoImpl<Specialite, Long> implements SpecialiteDao {

    public SpecialiteDaoImpl() {
        super(Specialite.class);
    }

    @Override
    public Optional<Specialite> findByNom(String nom) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Specialite s WHERE s.nom = :nom";
            return session.createQuery(hql, Specialite.class)
                    .setParameter("nom", nom)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de la spécialité par nom: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasMedecins(Long specialiteId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(m) FROM Medecin m WHERE m.specialite.id = :specialiteId";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("specialiteId", specialiteId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification des médecins de la spécialité: " + e.getMessage(), e);
        }
    }
}
