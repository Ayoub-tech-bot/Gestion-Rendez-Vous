<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Spécialités</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <div class="container mt-4">
        <h2>Gestion des Spécialités</h2>
        <hr>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.erreur}">
            <div class="alert alert-danger">${param.erreur}</div>
        </c:if>

        <!-- Formulaire d'ajout ou modification -->
        <div class="border p-3 mb-4 bg-light">
            <h4>
                <c:choose>
                    <c:when test="${not empty specialiteAEditer}">Modifier la Spécialité</c:when>
                    <c:otherwise>Ajouter une Spécialité</c:otherwise>
                </c:choose>
            </h4>

            <form action="${pageContext.request.contextPath}/admin/specialites" method="post">
                <input type="hidden" name="action" value="${not empty specialiteAEditer ? 'modifier' : 'creer'}">
                <c:if test="${not empty specialiteAEditer}">
                    <input type="hidden" name="id" value="${specialiteAEditer.id}">
                </c:if>

                <div class="mb-2">
                    <label>Nom :</label>
                    <input type="text" name="nom" class="form-control"
                           value="${specialiteAEditer.nom}" placeholder="Ex: Cardiologie" required>
                </div>

                <div class="mb-3">
                    <label>Description :</label>
                    <textarea name="description" class="form-control" rows="2"
                              placeholder="Description...">${specialiteAEditer.description}</textarea>
                </div>

                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${not empty specialiteAEditer}">Modifier</c:when>
                        <c:otherwise>Enregistrer</c:otherwise>
                    </c:choose>
                </button>
                <c:if test="${not empty specialiteAEditer}">
                    <a href="${pageContext.request.contextPath}/admin/specialites" class="btn btn-secondary">Annuler</a>
                </c:if>
            </form>
        </div>

        <!-- Tableau des spécialités -->
        <h4>Liste des Spécialités</h4>
        <table class="table table-bordered table-striped">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty specialites}">
                    <tr>
                        <td colspan="4" class="text-center">Aucune spécialité trouvée.</td>
                    </tr>
                </c:if>
                <c:forEach items="${specialites}" var="s">
                    <tr>
                        <td>${s.id}</td>
                        <td><strong>${s.nom}</strong></td>
                        <td>${s.description}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/specialites?action=edit&id=${s.id}" class="btn btn-sm btn-warning">Modifier</a>
                            <a href="${pageContext.request.contextPath}/admin/specialites?action=delete&id=${s.id}" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer cette spécialité ?');">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</body>
</html>
