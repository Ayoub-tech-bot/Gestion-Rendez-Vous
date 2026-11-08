package ma.ensa.gestion.rdv.servlet;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.model.Patient;
import ma.ensa.gestion.rdv.service.UtilisateurService;
import ma.ensa.gestion.rdv.service.impl.UtilisateurServiceImpl;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() {
        this.utilisateurService = new UtilisateurServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String nom = req.getParameter("nom");
            String prenom = req.getParameter("prenom");
            String email = req.getParameter("email");
            String telephone = req.getParameter("telephone");
            String dateNaissanceStr = req.getParameter("dateNaissance");
            String motDePasse = req.getParameter("motDePasse");

            Patient patient = new Patient();
            patient.setNom(nom);
            patient.setPrenom(prenom);
            patient.setEmail(email);
            patient.setTelephone(telephone);
            if (dateNaissanceStr != null && !dateNaissanceStr.isBlank()) {
                patient.setDateNaissance(LocalDate.parse(dateNaissanceStr));
            }
            patient.setMotDePasse(motDePasse);

            utilisateurService.inscrirePatient(patient);

            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}