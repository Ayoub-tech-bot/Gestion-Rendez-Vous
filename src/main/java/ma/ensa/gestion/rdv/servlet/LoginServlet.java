package ma.ensa.gestion.rdv.servlet;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.ensa.gestion.rdv.model.Role;
import ma.ensa.gestion.rdv.model.Utilisateur;
import ma.ensa.gestion.rdv.service.UtilisateurService;
import ma.ensa.gestion.rdv.service.impl.UtilisateurServiceImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() {
        this.utilisateurService = new UtilisateurServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            redirectByRole(user.getRole(), req, resp);
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String motDePasse = req.getParameter("motDePasse");

        Optional<Utilisateur> userOpt = utilisateurService.authentifier(email, motDePasse);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            redirectByRole(user.getRole(), req, resp);
        } else {
            req.setAttribute("erreur", "Email ou mot de passe incorrect.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }

    private void redirectByRole(Role role, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String contextPath = req.getContextPath();
        switch (role) {
            case ADMINISTRATEUR -> resp.sendRedirect(contextPath + "/admin/specialites");
            case MEDICINE -> resp.sendRedirect(contextPath + "/medecin/rendez-vous");
            case PATIENT -> resp.sendRedirect(contextPath + "/patient/rendez-vous");
        }
    }
}