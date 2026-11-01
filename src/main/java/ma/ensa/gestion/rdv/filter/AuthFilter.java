package ma.ensa.gestion.rdv.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.ensa.gestion.rdv.model.Role;
import ma.ensa.gestion.rdv.model.Utilisateur;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/medecin/*", "/patient/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String path = req.getServletPath();
        Role role = user.getRole();

        if (path.startsWith("/admin") && role != Role.ADMINISTRATEUR) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé à l'administrateur.");
            return;
        }
        if (path.startsWith("/medecin") && role != Role.MEDICINE) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux médecins.");
            return;
        }
        if (path.startsWith("/patient") && role != Role.PATIENT) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux patients.");
            return;
        }

        // 3. Authorized -> Continue to the requested Servlet
        chain.doFilter(request, response);
    }
}