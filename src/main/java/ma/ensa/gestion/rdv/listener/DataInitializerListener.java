package ma.ensa.gestion.rdv.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.ensa.gestion.rdv.dao.UtilisateurDao;
import ma.ensa.gestion.rdv.dao.impl.UtilisateurDaoImpl;
import ma.ensa.gestion.rdv.model.Administrateur;
import ma.ensa.gestion.rdv.model.Role;
import ma.ensa.gestion.rdv.model.Specialite;
import ma.ensa.gestion.rdv.service.SpecialiteService;
import ma.ensa.gestion.rdv.service.impl.SpecialiteServiceImpl;
import ma.ensa.gestion.rdv.util.HibernateUtil;
import ma.ensa.gestion.rdv.util.PasswordUtil;

@WebListener
public class DataInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println(">>> [DataInitializerListener] Verification des donnees initiales...");

            UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
            if (utilisateurDao.findByEmail("admin@rdv.ma").isEmpty()) {
                Administrateur admin = new Administrateur();
                admin.setNom("Admin");
                admin.setPrenom("Principal");
                admin.setEmail("admin@rdv.ma");
                admin.setMotDePasse(PasswordUtil.hashPassword("admin123"));
                admin.setRole(Role.ADMINISTRATEUR);
                utilisateurDao.save(admin);
                System.out.println(">>> [DataInitializerListener] Administrateur cree: admin@rdv.ma / admin123");
            }


            SpecialiteService specialiteService = new SpecialiteServiceImpl();
            if (specialiteService.listerTout().isEmpty()) {
                Specialite s1 = new Specialite();
                s1.setNom("Médecine Générale");
                s1.setDescription("Consultations générales et soins primaires");
                specialiteService.creer(s1);

                Specialite s2 = new Specialite();
                s2.setNom("Cardiologie");
                s2.setDescription("Maladies cardiovasculaires et du cœur");
                specialiteService.creer(s2);

                Specialite s3 = new Specialite();
                s3.setNom("Pédiatrie");
                s3.setDescription("Santé et développement de l'enfant");
                specialiteService.creer(s3);

                Specialite s4 = new Specialite();
                s4.setNom("Dermatologie");
                s4.setDescription("Maladies de la peau et des muqueuses");
                specialiteService.creer(s4);

                System.out.println(">>> [DataInitializerListener] Specialites initiales creees.");
            }

            System.out.println(">>> [DataInitializerListener] Initialisation terminee avec succes.");
        } catch (Throwable t) {
            System.err.println(">>> [DataInitializerListener] Erreur d'initialisation: " + t.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (HibernateUtil.getSessionFactory() != null && !HibernateUtil.getSessionFactory().isClosed()) {
                HibernateUtil.getSessionFactory().close();
            }
        } catch (Throwable ignored) {}
    }
}
