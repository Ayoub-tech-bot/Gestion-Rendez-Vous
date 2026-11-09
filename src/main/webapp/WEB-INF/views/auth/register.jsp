<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription Patient - Gestion des Rendez-Vous</title>
</head>
<body>

    <h2>Créer un compte Patient</h2>

    <% String erreur = (String) request.getAttribute("erreur");
       if (erreur != null) { %>
        <p><%= erreur %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">
        <div>
            <label for="nom">Nom :</label><br>
            <input type="text" id="nom" name="nom"
                   value="<%= request.getParameter("nom") != null ? request.getParameter("nom") : "" %>" required>
        </div>
        <br>
        <div>
            <label for="prenom">Prénom :</label><br>
            <input type="text" id="prenom" name="prenom"
                   value="<%= request.getParameter("prenom") != null ? request.getParameter("prenom") : "" %>" required>
        </div>
        <br>
        <div>
            <label for="email">Email :</label><br>
            <input type="email" id="email" name="email"
                   value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
        </div>
        <br>
        <div>
            <label for="telephone">Téléphone :</label><br>
            <input type="tel" id="telephone" name="telephone"
                   value="<%= request.getParameter("telephone") != null ? request.getParameter("telephone") : "" %>" required>
        </div>
        <br>
        <div>
            <label for="dateNaissance">Date de naissance :</label><br>
            <input type="date" id="dateNaissance" name="dateNaissance"
                   value="<%= request.getParameter("dateNaissance") != null ? request.getParameter("dateNaissance") : "" %>" required>
        </div>
        <br>
        <div>
            <label for="motDePasse">Mot de passe (min. 6 caractères) :</label><br>
            <input type="password" id="motDePasse" name="motDePasse" minlength="6" required>
        </div>
        <br>
        <button type="submit">S'inscrire</button>
    </form>

    <hr>

    <p>
        Déjà inscrit ?
        <a href="<%= request.getContextPath() %>/login">Se connecter</a>
    </p>

</body>
</html>