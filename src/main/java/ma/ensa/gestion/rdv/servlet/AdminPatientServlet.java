package ma.ensa.gestion.rdv.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.service.UtilisateurService;
import ma.ensa.gestion.rdv.service.impl.UtilisateurServiceImpl;

import java.io.IOException;

@WebServlet("/admin/patients")
public class AdminPatientServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() {
        this.utilisateurService = new UtilisateurServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("patients", utilisateurService.listerPatients());
        req.getRequestDispatcher("/WEB-INF/views/admin/patients.jsp").forward(req, resp);
    }
}

