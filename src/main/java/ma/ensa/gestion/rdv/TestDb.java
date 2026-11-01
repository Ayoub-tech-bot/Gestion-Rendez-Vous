package ma.ensa.gestion.rdv;

import ma.ensa.gestion.rdv.util.HibernateUtil;
import org.hibernate.Session;

public class TestDb {
    public static void main(String[] args) {
        System.out.println("Starting Hibernate...");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Success! Database and tables are created in MySQL.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }
}