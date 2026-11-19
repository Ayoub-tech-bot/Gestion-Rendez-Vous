<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container my-3 pb-2 border-bottom">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h4 class="d-inline-block me-3 text-primary">Gestion RDV</h4>
            <c:choose>
                <c:when test="${sessionScope.user.role == 'ADMINISTRATEUR'}">
                    <a href="${pageContext.request.contextPath}/admin/medecins" class="btn btn-sm btn-link">Médecins</a> |
                    <a href="${pageContext.request.contextPath}/admin/specialites" class="btn btn-sm btn-link">Spécialités</a> |
                    <a href="${pageContext.request.contextPath}/admin/patients" class="btn btn-sm btn-link">Patients</a> |
                    <a href="${pageContext.request.contextPath}/admin/rendez-vous" class="btn btn-sm btn-link">Rendez-Vous</a>
                </c:when>
                <c:when test="${sessionScope.user.role == 'MEDICINE'}">
                    <a href="${pageContext.request.contextPath}/medecin/rendez-vous" class="btn btn-sm btn-link">Mon Agenda & Demandes</a>
                </c:when>
                <c:when test="${sessionScope.user.role == 'PATIENT'}">
                    <a href="${pageContext.request.contextPath}/patient/rendez-vous" class="btn btn-sm btn-link">Mes Rendez-Vous</a>
                </c:when>
            </c:choose>
        </div>
        <div>
            <c:if test="${not empty sessionScope.user}">
                <span>Connecté : <strong>${sessionScope.user.prenom} ${sessionScope.user.nom}</strong> (${sessionScope.user.role})</span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-danger ms-2">Déconnexion</a>
            </c:if>
        </div>
    </div>
</div>
