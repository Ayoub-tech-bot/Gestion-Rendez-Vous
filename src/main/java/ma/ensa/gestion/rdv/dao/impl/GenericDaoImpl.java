package ma.ensa.gestion.rdv.dao.impl;

import ma.ensa.gestion.rdv.dao.GenericDao;
import ma.ensa.gestion.rdv.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {

    protected final Class<T> entityClass;

    public GenericDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erreur lors de l'enregistrement de l'entité: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public T update(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T merged = session.merge(entity);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erreur lors de la mise à jour de l'entité: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(ID id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erreur lors de la suppression de l'entité: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            return session.createQuery(hql, entityClass).list();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la liste: " + e.getMessage(), e);
        }
    }
}
