<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agenda Médecin - Gestion des Rendez-Vous</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <div class="container mt-4">
        <h2>Espace Médecin - Gestion des Rendez-Vous</h2>
        <hr>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.erreur}">
            <div class="alert alert-danger">${param.erreur}</div>
        </c:if>

        <!-- Filtres par date et statut -->
        <div class="border p-3 mb-4 bg-light">
            <h5>Filtrer mon agenda</h5>
            <form action="${pageContext.request.contextPath}/medecin/rendez-vous" method="get" class="row g-2 align-items-end">
                <div class="col-md-4">
                    <label>Date :</label>
                    <input type="date" name="date" class="form-control" value="${param.date}">
                </div>
                <div class="col-md-4">
                    <label>Statut :</label>
                    <select name="statut" class="form-select">
                        <option value="">-- Tous les statuts --</option>
                        <option value="EN_ATTENTE" <c:if test="${param.statut == 'EN_ATTENTE'}">selected</c:if>>EN ATTENTE</option>
                        <option value="CONFIRME" <c:if test="${param.statut == 'CONFIRME'}">selected</c:if>>CONFIRMÉ</option>
                        <option value="TERMINE" <c:if test="${param.statut == 'TERMINE'}">selected</c:if>>TERMINÉ</option>
                        <option value="REFUSE" <c:if test="${param.statut == 'REFUSE'}">selected</c:if>>REFUSÉ</option>
                        <option value="ANNULE" <c:if test="${param.statut == 'ANNULE'}">selected</c:if>>ANNULÉ</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary">Filtrer</button>
                    <a href="${pageContext.request.contextPath}/medecin/rendez-vous" class="btn btn-secondary">Réinitialiser</a>
                </div>
            </form>
        </div>

        <!-- Tableau des rendez-vous du médecin -->
        <h4>Liste des Rendez-Vous</h4>
        <table class="table table-bordered table-striped">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Date</th>
                    <th>Patient</th>
                    <th>Téléphone</th>
                    <th>Motif</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty rendezVousList}">
                    <tr>
                        <td colspan="7" class="text-center">Aucun rendez-vous trouvé.</td>
                    </tr>
                </c:if>
                <c:forEach items="${rendezVousList}" var="r">
                    <tr>
                        <td>${r.id}</td>
                        <td>${r.date}</td>
                        <td>${r.patient.prenom} ${r.patient.nom}</td>
                        <td>${not empty r.patient.telephone ? r.patient.telephone : '-'}</td>
                        <td>${r.motif}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.status == 'EN_ATTENTE'}"><span class="badge bg-warning text-dark">EN ATTENTE</span></c:when>
                                <c:when test="${r.status == 'CONFIRME'}"><span class="badge bg-success">CONFIRMÉ</span></c:when>
                                <c:when test="${r.status == 'TERMINE'}"><span class="badge bg-secondary">TERMINÉ</span></c:when>
                                <c:when test="${r.status == 'REFUSE'}"><span class="badge bg-danger">REFUSÉ</span></c:when>
                                <c:when test="${r.status == 'ANNULE'}"><span class="badge bg-dark">ANNULÉ</span></c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${r.status == 'EN_ATTENTE'}">
                                    <a href="${pageContext.request.contextPath}/medecin/rendez-vous?action=confirmer&id=${r.id}" class="btn btn-sm btn-success">Confirmer</a>
                                    <a href="${pageContext.request.contextPath}/medecin/rendez-vous?action=refuser&id=${r.id}" class="btn btn-sm btn-danger" onclick="return confirm('Refuser cette demande ?');">Refuser</a>
                                </c:when>
                                <c:when test="${r.status == 'CONFIRME'}">
                                    <a href="${pageContext.request.contextPath}/medecin/rendez-vous?action=terminer&id=${r.id}" class="btn btn-sm btn-primary">Terminer</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">-</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</body>
</html>
