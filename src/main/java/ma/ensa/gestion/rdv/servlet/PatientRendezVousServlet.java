package ma.ensa.gestion.rdv.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.model.Medecin;
import ma.ensa.gestion.rdv.model.Patient;
import ma.ensa.gestion.rdv.model.RendezVous;
import ma.ensa.gestion.rdv.model.Utilisateur;
import ma.ensa.gestion.rdv.service.MedecinService;
import ma.ensa.gestion.rdv.service.RendezVousService;
import ma.ensa.gestion.rdv.service.SpecialiteService;
import ma.ensa.gestion.rdv.service.impl.MedecinServiceImpl;
import ma.ensa.gestion.rdv.service.impl.RendezVousServiceImpl;
import ma.ensa.gestion.rdv.service.impl.SpecialiteServiceImpl;

@WebServlet("/patient/rendez-vous")
public class PatientRendezVousServlet extends HttpServlet {

    private RendezVousService rendezVousService;
    private MedecinService medecinService;
    private SpecialiteService specialiteService;

    @Override
    public void init() {
        this.rendezVousService = new RendezVousServiceImpl();
        this.medecinService = new MedecinServiceImpl();
        this.specialiteService = new SpecialiteServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if ("annuler".equals(action) && idStr != null) {
            try {
                rendezVousService.annulerRendezVous(Long.parseLong(idStr), user.getId());
                String msg = URLEncoder.encode("Rendez-vous annulé avec succès.", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/patient/rendez-vous?success=" + msg);
                return;
            } catch (Exception e) {
                String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/patient/rendez-vous?erreur=" + errMsg);
                return;
            }
        }

        if ("reporter".equals(action) && idStr != null) {
            rendezVousService.trouverParId(Long.parseLong(idStr))
                    .ifPresent(r -> {
                        req.setAttribute("rdvAReporter", r);
                        String dateStr = req.getParameter("date");
                        LocalDate reportDate = (dateStr != null && !dateStr.isBlank()) ? LocalDate.parse(dateStr) : r.getDate();
                        req.setAttribute("creneauxDisponiblesReport",
                                rendezVousService.getCreneauxDisponibles(r.getMedecin().getId(), reportDate));
                    });
        }

        String specialiteIdStr = req.getParameter("specialiteId");
        if (specialiteIdStr != null && !specialiteIdStr.isBlank()) {
            req.setAttribute("medecins", medecinService.listerParSpecialite(Long.parseLong(specialiteIdStr)));
        } else {
            req.setAttribute("medecins", medecinService.listerTout());
        }

        // Si le patient a sélectionné un médecin et une date, calculer les créneaux libres (Scénario S5)
        String medecinIdChoisi = req.getParameter("medecinId");
        String dateChoisie = req.getParameter("date");
        if (medecinIdChoisi != null && !medecinIdChoisi.isBlank() && dateChoisie != null && !dateChoisie.isBlank()) {
            try {
                Long mId = Long.parseLong(medecinIdChoisi);
                LocalDate d = LocalDate.parse(dateChoisie);
                req.setAttribute("creneauxDisponibles", rendezVousService.getCreneauxDisponibles(mId, d));
            } catch (Exception ignored) {}
        }

        req.setAttribute("specialites", specialiteService.listerTout());
        req.setAttribute("mesRendezVous", rendezVousService.listerParPatient(user.getId()));
        req.getRequestDispatcher("/WEB-INF/views/patient/rendez-vous.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
        String action = req.getParameter("action");

        try {
            if ("reporter".equals(action)) {
                Long rdvId = Long.parseLong(req.getParameter("id"));
                LocalDate nouvelleDate = LocalDate.parse(req.getParameter("date"));
                String heureStr = req.getParameter("heureDebut");
                LocalTime nouvelleHeure = (heureStr != null && !heureStr.isBlank()) ? LocalTime.parse(heureStr) : null;

                rendezVousService.reporterRendezVous(rdvId, user.getId(), nouvelleDate, nouvelleHeure);
                String msg = URLEncoder.encode("Rendez-vous reporté avec succès.", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/patient/rendez-vous?success=" + msg);
            } else {
                Long medecinId = Long.parseLong(req.getParameter("medecinId"));
                LocalDate date = LocalDate.parse(req.getParameter("date"));
                String heureStr = req.getParameter("heureDebut");
                LocalTime heureDebut = (heureStr != null && !heureStr.isBlank()) ? LocalTime.parse(heureStr) : null;
                String motif = req.getParameter("motif");

                Medecin medecin = medecinService.trouverParId(medecinId)
                        .orElseThrow(() -> new IllegalArgumentException("Médecin introuvable."));

                Patient patient = new Patient();
                patient.setId(user.getId());

                RendezVous rdv = new RendezVous();
                rdv.setPatient(patient);
                rdv.setMedecin(medecin);
                rdv.setDate(date);
                rdv.setHeureDebut(heureDebut);
                rdv.setMotif(motif);

                rendezVousService.prendreRendezVous(rdv);
                String msg = URLEncoder.encode("Demande de rendez-vous enregistrée (En attente).", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/patient/rendez-vous?success=" + msg);
            }
        } catch (Exception e) {
            String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/patient/rendez-vous?erreur=" + errMsg);
        }
    }
}