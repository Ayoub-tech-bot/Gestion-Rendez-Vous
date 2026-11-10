<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Médecins</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <div class="container mt-4">
        <h2>Gestion des Médecins</h2>
        <hr>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.erreur}">
            <div class="alert alert-danger">${param.erreur}</div>
        </c:if>


        <div class="border p-3 mb-4 bg-light">
            <h4>
                <c:choose>
                    <c:when test="${not empty medecinAEditer}">Modifier le Médecin</c:when>
                    <c:otherwise>Ajouter un Médecin</c:otherwise>
                </c:choose>
            </h4>

            <form action="${pageContext.request.contextPath}/admin/medecins" method="post">
                <input type="hidden" name="action" value="${not empty medecinAEditer ? 'modifier' : 'creer'}">
                <c:if test="${not empty medecinAEditer}">
                    <input type="hidden" name="id" value="${medecinAEditer.id}">
                </c:if>

                <div class="row mb-2">
                    <div class="col">
                        <label>Nom :</label>
                        <input type="text" name="nom" class="form-control" value="${medecinAEditer.nom}" required>
                    </div>
                    <div class="col">
                        <label>Prénom :</label>
                        <input type="text" name="prenom" class="form-control" value="${medecinAEditer.prenom}" required>
                    </div>
                </div>

                <div class="row mb-2">
                    <div class="col">
                        <label>Email :</label>
                        <input type="email" name="email" class="form-control" value="${medecinAEditer.email}" required>
                    </div>
                    <div class="col">
                        <label>Mot de passe :</label>
                        <input type="password" name="motDePasse" class="form-control"
                               <c:if test="${empty medecinAEditer}">required minlength="6"</c:if>
                               placeholder="${not empty medecinAEditer ? 'Laisser vide pour ne pas changer' : 'Minimum 6 caractères'}">
                    </div>
                </div>

                <div class="mb-3">
                    <label>Spécialité :</label>
                    <select name="specialiteId" class="form-select" required>
                        <option value="">-- Choisir une spécialité --</option>
                        <c:forEach items="${specialites}" var="spec">
                            <option value="${spec.id}" <c:if test="${medecinAEditer.specialite.id == spec.id}">selected</c:if>>
                                ${spec.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${not empty medecinAEditer}">Modifier</c:when>
                        <c:otherwise>Enregistrer</c:otherwise>
                    </c:choose>
                </button>
                <c:if test="${not empty medecinAEditer}">
                    <a href="${pageContext.request.contextPath}/admin/medecins" class="btn btn-secondary">Annuler</a>
                </c:if>
            </form>
        </div>

        <!-- Tableau des médecins -->
        <h4>Liste des Médecins</h4>
        <table class="table table-bordered table-striped">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Spécialité</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty medecins}">
                    <tr>
                        <td colspan="6" class="text-center">Aucun médecin trouvé.</td>
                    </tr>
                </c:if>
                <c:forEach items="${medecins}" var="m">
                    <tr>
                        <td>${m.id}</td>
                        <td>${m.nom}</td>
                        <td>${m.prenom}</td>
                        <td>${m.email}</td>
                        <td>${m.specialite.nom}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/medecins?action=edit&id=${m.id}" class="btn btn-sm btn-warning">Modifier</a>
                            <a href="${pageContext.request.contextPath}/admin/medecins?action=delete&id=${m.id}" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ce médecin ?');">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</body>
</html>
