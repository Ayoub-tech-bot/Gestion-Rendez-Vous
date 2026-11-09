<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Connexion - Gestion des Rendez-Vous</title>
</head>
<body>

    <h2>Connexion</h2>

    <% if ("true".equals(request.getParameter("registered"))) { %>
        <p>Inscription réussie ! Vous pouvez maintenant vous connecter.</p>
    <% } %>

    <% String erreur = (String) request.getAttribute("erreur");
       if (erreur != null) { %>
        <p><%= erreur %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <div>
            <label for="email">Email :</label><br>
            <input type="email" id="email" name="email" required>
        </div>
        <br>
        <div>
            <label for="motDePasse">Mot de passe :</label><br>
            <input type="password" id="motDePasse" name="motDePasse" required>
        </div>
        <br>
        <button type="submit">Se connecter</button>
    </form>

    <hr>

    <p>
        Pas encore de compte ?
        <a href="<%= request.getContextPath() %>/register">Créer un compte patient</a>
    </p>

</body>
</html>