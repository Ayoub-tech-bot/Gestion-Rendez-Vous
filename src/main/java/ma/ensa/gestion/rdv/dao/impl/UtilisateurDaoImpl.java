package ma.ensa.gestion.rdv.dao.impl;

import ma.ensa.gestion.rdv.dao.UtilisateurDao;
import ma.ensa.gestion.rdv.model.Utilisateur;
import ma.ensa.gestion.rdv.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class UtilisateurDaoImpl extends GenericDaoImpl<Utilisateur, Long> implements UtilisateurDao {

    public UtilisateurDaoImpl() {
        super(Utilisateur.class);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Utilisateur u WHERE u.email = :email";
            return session.createQuery(hql, Utilisateur.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur par email: " + e.getMessage(), e);
        }
    }
}
