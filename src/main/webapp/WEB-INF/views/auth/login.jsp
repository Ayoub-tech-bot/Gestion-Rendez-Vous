<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="container mt-5" style="max-width: 450px;">
        <h2 class="text-center mb-4">Connexion</h2>

        <% if ("true".equals(request.getParameter("registered"))) { %>
            <div class="alert alert-success">Inscription réussie ! Vous pouvez vous connecter.</div>
        <% } %>

        <% String erreur = (String) request.getAttribute("erreur");
           if (erreur != null) { %>
            <div class="alert alert-danger"><%= erreur %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/login" method="post" class="border p-4 bg-light">
            <div class="mb-3">
                <label>Email :</label>
                <input type="email" name="email" class="form-control" required>
            </div>

            <div class="mb-3">
                <label>Mot de passe :</label>
                <input type="password" name="motDePasse" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-primary w-100">Se connecter</button>
        </form>

        <p class="text-center mt-3">
            Pas encore de compte ?
            <a href="<%= request.getContextPath() %>/register">Créer un compte patient</a>
        </p>
    </div>

</body>
</html>
