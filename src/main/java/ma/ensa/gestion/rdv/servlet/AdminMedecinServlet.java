package ma.ensa.gestion.rdv.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.model.Medecin;
import ma.ensa.gestion.rdv.model.Specialite;
import ma.ensa.gestion.rdv.service.MedecinService;
import ma.ensa.gestion.rdv.service.SpecialiteService;
import ma.ensa.gestion.rdv.service.impl.MedecinServiceImpl;
import ma.ensa.gestion.rdv.service.impl.SpecialiteServiceImpl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/admin/medecins")
public class AdminMedecinServlet extends HttpServlet {

    private MedecinService medecinService;
    private SpecialiteService specialiteService;

    @Override
    public void init() {
        this.medecinService = new MedecinServiceImpl();
        this.specialiteService = new SpecialiteServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if ("delete".equals(action) && idStr != null) {
            try {
                medecinService.supprimer(Long.parseLong(idStr));
                resp.sendRedirect(req.getContextPath() + "/admin/medecins?success=Médecin supprimé avec succès.");
                return;
            } catch (Exception e) {
                String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/admin/medecins?erreur=" + errMsg);
                return;
            }
        }

        if ("edit".equals(action) && idStr != null) {
            try {
                medecinService.trouverParId(Long.parseLong(idStr))
                        .ifPresent(m -> req.setAttribute("medecinAEditer", m));
            } catch (Exception ignored) {}
        }

        req.setAttribute("medecins", medecinService.listerTout());
        req.setAttribute("specialites", specialiteService.listerTout());
        req.getRequestDispatcher("/WEB-INF/views/admin/medecins.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            String nom = req.getParameter("nom");
            String prenom = req.getParameter("prenom");
            String email = req.getParameter("email");
            String motDePasse = req.getParameter("motDePasse");
            String specialiteIdStr = req.getParameter("specialiteId");

            if (specialiteIdStr == null || specialiteIdStr.isBlank()) {
                throw new IllegalArgumentException("Veuillez sélectionner une spécialité.");
            }

            Specialite specialite = specialiteService.trouverParId(Long.parseLong(specialiteIdStr))
                    .orElseThrow(() -> new IllegalArgumentException("Spécialité sélectionnée introuvable."));

            if ("modifier".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                Medecin medecin = new Medecin();
                medecin.setId(id);
                medecin.setNom(nom);
                medecin.setPrenom(prenom);
                medecin.setEmail(email);
                medecin.setSpecialite(specialite);
                if (motDePasse != null && !motDePasse.isBlank()) {
                    medecin.setMotDePasse(motDePasse);
                }
                medecinService.modifier(medecin);
                resp.sendRedirect(req.getContextPath() + "/admin/medecins?success=Médecin mis à jour avec succès.");
            } else {
                // Action: creer
                Medecin medecin = new Medecin();
                medecin.setNom(nom);
                medecin.setPrenom(prenom);
                medecin.setEmail(email);
                medecin.setMotDePasse(motDePasse);
                medecin.setSpecialite(specialite);

                medecinService.creer(medecin);
                resp.sendRedirect(req.getContextPath() + "/admin/medecins?success=Médecin créé avec succès.");
            }
        } catch (Exception e) {
            String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/admin/medecins?erreur=" + errMsg);
        }
    }
}

