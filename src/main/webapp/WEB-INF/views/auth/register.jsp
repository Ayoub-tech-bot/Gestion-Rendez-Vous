<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription Patient</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="container mt-5" style="max-width: 500px;">
        <h2 class="text-center mb-4">Inscription Patient</h2>

        <% String erreur = (String) request.getAttribute("erreur");
           if (erreur != null) { %>
            <div class="alert alert-danger"><%= erreur %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/register" method="post" class="border p-4 bg-light">
            <div class="row mb-2">
                <div class="col">
                    <label>Nom :</label>
                    <input type="text" name="nom" class="form-control"
                           value="<%= request.getParameter("nom") != null ? request.getParameter("nom") : "" %>" required>
                </div>
                <div class="col">
                    <label>Prénom :</label>
                    <input type="text" name="prenom" class="form-control"
                           value="<%= request.getParameter("prenom") != null ? request.getParameter("prenom") : "" %>" required>
                </div>
            </div>

            <div class="mb-2">
                <label>Email :</label>
                <input type="email" name="email" class="form-control"
                       value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
            </div>

            <div class="mb-2">
                <label>Téléphone :</label>
                <input type="tel" name="telephone" class="form-control"
                       value="<%= request.getParameter("telephone") != null ? request.getParameter("telephone") : "" %>" required>
            </div>

            <div class="mb-2">
                <label>Date de naissance :</label>
                <input type="date" name="dateNaissance" class="form-control"
                       value="<%= request.getParameter("dateNaissance") != null ? request.getParameter("dateNaissance") : "" %>" required>
            </div>

            <div class="mb-3">
                <label>Mot de passe :</label>
                <input type="password" name="motDePasse" class="form-control" minlength="6" required>
            </div>

            <button type="submit" class="btn btn-primary w-100">S'inscrire</button>
        </form>

        <p class="text-center mt-3">
            Déjà un compte ?
            <a href="<%= request.getContextPath() %>/login">Se connecter</a>
        </p>
    </div>

</body>
</html>
