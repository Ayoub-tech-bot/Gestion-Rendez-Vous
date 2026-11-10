package ma.ensa.gestion.rdv.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.gestion.rdv.model.Specialite;
import ma.ensa.gestion.rdv.service.SpecialiteService;
import ma.ensa.gestion.rdv.service.impl.SpecialiteServiceImpl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/admin/specialites")
public class AdminSpecialiteServlet extends HttpServlet {

    private SpecialiteService specialiteService;

    @Override
    public void init() {
        this.specialiteService = new SpecialiteServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if ("delete".equals(action) && idStr != null) {
            try {
                specialiteService.supprimer(Long.parseLong(idStr));
                resp.sendRedirect(req.getContextPath() + "/admin/specialites?success=Spécialité supprimée avec succès.");
                return;
            } catch (Exception e) {
                String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/admin/specialites?erreur=" + errMsg);
                return;
            }
        }

        if ("edit".equals(action) && idStr != null) {
            try {
                specialiteService.trouverParId(Long.parseLong(idStr))
                        .ifPresent(s -> req.setAttribute("specialiteAEditer", s));
            } catch (Exception ignored) {}
        }

        req.setAttribute("specialites", specialiteService.listerTout());
        req.getRequestDispatcher("/WEB-INF/views/admin/specialites.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("modifier".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                String nom = req.getParameter("nom");
                String description = req.getParameter("description");

                Specialite s = new Specialite(id, nom, description);
                specialiteService.modifier(s);
                resp.sendRedirect(req.getContextPath() + "/admin/specialites?success=Spécialité mise à jour avec succès.");
            } else {
                // Action: creer
                String nom = req.getParameter("nom");
                String description = req.getParameter("description");

                Specialite s = new Specialite();
                s.setNom(nom);
                s.setDescription(description);

                specialiteService.creer(s);
                resp.sendRedirect(req.getContextPath() + "/admin/specialites?success=Spécialité ajoutée avec succès.");
            }
        } catch (Exception e) {
            String errMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/admin/specialites?erreur=" + errMsg);
        }
    }
}

