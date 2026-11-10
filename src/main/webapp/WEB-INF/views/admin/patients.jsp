<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patients Inscrits</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <div class="container mt-4">
        <h2>Liste des Patients Inscrits</h2>
        <hr>

        <table class="table table-bordered table-striped">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Téléphone</th>
                    <th>Date de Naissance</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty patients}">
                    <tr>
                        <td colspan="6" class="text-center">Aucun patient inscrit pour le moment.</td>
                    </tr>
                </c:if>
                <c:forEach items="${patients}" var="p">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.nom}</td>
                        <td>${p.prenom}</td>
                        <td>${p.email}</td>
                        <td>${not empty p.telephone ? p.telephone : '-'}</td>
                        <td>${not empty p.dateNaissance ? p.dateNaissance : '-'}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</body>
</html>
