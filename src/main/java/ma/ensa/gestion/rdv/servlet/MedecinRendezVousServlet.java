package ma.ensa.gestion.rdv.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.model.StatutRendezVous;
import ma.ensa.gestion.rdv.model.Utilisateur;
import ma.ensa.gestion.rdv.service.RendezVousService;
import ma.ensa.gestion.rdv.service.impl.RendezVousServiceImpl;

@WebServlet("/medecin/rendez-vous")
public class MedecinRendezVousServlet extends HttpServlet {

    private RendezVousService rendezVousService;

    @Override
    public void init() {
        this.rendezVousService = new RendezVousServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        Long medecinId = user.getId();

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        
        if (action != null && idStr != null) {
            try {
                Long rdvId = Long.parseLong(idStr);
                switch (action) {
                    case "confirmer" -> rendezVousService.confirmerRendezVous(rdvId, medecinId);
                    case "refuser" -> rendezVousService.refuserRendezVous(rdvId, medecinId);
                    case "terminer" -> rendezVousService.terminerRendezVous(rdvId, medecinId);
                }
                String msg = URLEncoder.encode("Statut du rendez-vous mis à jour.", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/medecin/rendez-vous?success=" + msg);
                return;
            } catch (Exception e) {
                String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/medecin/rendez-vous?erreur=" + errMsg);
                return;
            }
        }

        String dateStr = req.getParameter("date");
        String statutStr = req.getParameter("statut");

        LocalDate date = (dateStr != null && !dateStr.isBlank()) ? LocalDate.parse(dateStr) : null;
        StatutRendezVous statut = (statutStr != null && !statutStr.isBlank()) ? StatutRendezVous.valueOf(statutStr) : null;

        req.setAttribute("rendezVousList", rendezVousService.listerAvecFiltres(medecinId, date, statut));
        req.getRequestDispatcher("/WEB-INF/views/medecin/rendez-vous.jsp").forward(req, resp);
    }
}