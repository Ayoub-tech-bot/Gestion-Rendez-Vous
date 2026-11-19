package ma.ensa.gestion.rdv.servlet;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.model.StatutRendezVous;
import ma.ensa.gestion.rdv.service.MedecinService;
import ma.ensa.gestion.rdv.service.RendezVousService;
import ma.ensa.gestion.rdv.service.impl.MedecinServiceImpl;
import ma.ensa.gestion.rdv.service.impl.RendezVousServiceImpl;

@WebServlet("/admin/rendez-vous")
public class AdminRendezVousServlet extends HttpServlet {

    private RendezVousService rendezVousService;
    private MedecinService medecinService;

    @Override
    public void init() {
        this.rendezVousService = new RendezVousServiceImpl();
        this.medecinService = new MedecinServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String medecinIdStr = req.getParameter("medecinId");
        String dateStr = req.getParameter("date");
        String statutStr = req.getParameter("statut");

        Long medecinId = (medecinIdStr != null && !medecinIdStr.isBlank()) ? Long.parseLong(medecinIdStr) : null;
        LocalDate date = (dateStr != null && !dateStr.isBlank()) ? LocalDate.parse(dateStr) : null;
        StatutRendezVous statut = (statutStr != null && !statutStr.isBlank()) ? StatutRendezVous.valueOf(statutStr) : null;

        req.setAttribute("medecins", medecinService.listerTout());
        req.setAttribute("rendezVousList", rendezVousService.listerAvecFiltres(medecinId, date, statut));
        req.getRequestDispatcher("/WEB-INF/views/admin/rendez-vous.jsp").forward(req, resp);
    }
}