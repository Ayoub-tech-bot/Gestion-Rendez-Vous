<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mes Rendez-Vous - Espace Patient</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <div class="container mt-4">
        <h2>Espace Patient - Mes Rendez-Vous</h2>
        <hr>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.erreur}">
            <div class="alert alert-danger">${param.erreur}</div>
        </c:if>

        <!-- Formulaire de prise ou de report de rendez-vous -->
        <div class="border p-3 mb-4 bg-light">
            <c:choose>
                <c:when test="${not empty rdvAReporter}">
                    <h4>Reporter le Rendez-Vous #${rdvAReporter.id}</h4>
                    <p class="text-muted mb-2">
                        Médecin : <strong>Dr. ${rdvAReporter.medecin.prenom} ${rdvAReporter.medecin.nom}</strong>
                        (${rdvAReporter.medecin.specialite.nom}) — Date actuelle : <strong>${rdvAReporter.date}</strong>
                    </p>
                    <form action="${pageContext.request.contextPath}/patient/rendez-vous" method="post">
                        <input type="hidden" name="action" value="reporter">
                        <input type="hidden" name="id" value="${rdvAReporter.id}">

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label>Nouvelle Date (Lundi au Vendredi) :</label>
                                <input type="date" name="date" class="form-control" value="${rdvAReporter.date}" required>
                            </div>
                            <div class="col-md-6">
                                <label>Nouveau Créneau Horaire (30 min) :</label>
                                <select name="heureDebut" class="form-select" required>
                                    <option value="">-- Choisir un créneau --</option>
                                    <option value="08:00" <c:if test="${rdvAReporter.heureDebut == '08:00'}">selected</c:if>>08:00 - 08:30</option>
                                    <option value="08:30" <c:if test="${rdvAReporter.heureDebut == '08:30'}">selected</c:if>>08:30 - 09:00</option>
                                    <option value="09:00" <c:if test="${rdvAReporter.heureDebut == '09:00'}">selected</c:if>>09:00 - 09:30</option>
                                    <option value="09:30" <c:if test="${rdvAReporter.heureDebut == '09:30'}">selected</c:if>>09:30 - 10:00</option>
                                    <option value="10:00" <c:if test="${rdvAReporter.heureDebut == '10:00'}">selected</c:if>>10:00 - 10:30</option>
                                    <option value="10:30" <c:if test="${rdvAReporter.heureDebut == '10:30'}">selected</c:if>>10:30 - 11:00</option>
                                    <option value="11:00" <c:if test="${rdvAReporter.heureDebut == '11:00'}">selected</c:if>>11:00 - 11:30</option>
                                    <option value="11:30" <c:if test="${rdvAReporter.heureDebut == '11:30'}">selected</c:if>>11:30 - 12:00</option>
                                    <option value="12:00" <c:if test="${rdvAReporter.heureDebut == '12:00'}">selected</c:if>>12:00 - 12:30</option>
                                    <option value="12:30" <c:if test="${rdvAReporter.heureDebut == '12:30'}">selected</c:if>>12:30 - 13:00</option>
                                    <option value="13:00" <c:if test="${rdvAReporter.heureDebut == '13:00'}">selected</c:if>>13:00 - 13:30</option>
                                    <option value="13:30" <c:if test="${rdvAReporter.heureDebut == '13:30'}">selected</c:if>>13:30 - 14:00</option>
                                    <option value="14:00" <c:if test="${rdvAReporter.heureDebut == '14:00'}">selected</c:if>>14:00 - 14:30</option>
                                    <option value="14:30" <c:if test="${rdvAReporter.heureDebut == '14:30'}">selected</c:if>>14:30 - 15:00</option>
                                    <option value="15:00" <c:if test="${rdvAReporter.heureDebut == '15:00'}">selected</c:if>>15:00 - 15:30</option>
                                    <option value="15:30" <c:if test="${rdvAReporter.heureDebut == '15:30'}">selected</c:if>>15:30 - 16:00</option>
                                    <option value="16:00" <c:if test="${rdvAReporter.heureDebut == '16:00'}">selected</c:if>>16:00 - 16:30</option>
                                    <option value="16:30" <c:if test="${rdvAReporter.heureDebut == '16:30'}">selected</c:if>>16:30 - 17:00</option>
                                    <option value="17:00" <c:if test="${rdvAReporter.heureDebut == '17:00'}">selected</c:if>>17:00 - 17:30</option>
                                    <option value="17:30" <c:if test="${rdvAReporter.heureDebut == '17:30'}">selected</c:if>>17:30 - 18:00</option>
                                </select>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary">Confirmer le report</button>
                        <a href="${pageContext.request.contextPath}/patient/rendez-vous" class="btn btn-secondary">Annuler</a>
                    </form>
                </c:when>

                <c:otherwise>
                    <h4>Prendre un Rendez-Vous</h4>

                    <!-- Filtre par spécialité (GET) -->
                    <form action="${pageContext.request.contextPath}/patient/rendez-vous" method="get" class="row g-2 align-items-end mb-3">
                        <div class="col-md-6">
                            <label>Filtrer les médecins par spécialité :</label>
                            <select name="specialiteId" class="form-select" onchange="this.form.submit()">
                                <option value="">-- Toutes les spécialités --</option>
                                <c:forEach items="${specialites}" var="spec">
                                    <option value="${spec.id}" <c:if test="${param.specialiteId == spec.id}">selected</c:if>>
                                        ${spec.nom}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>

                    <!-- Formulaire de réservation (POST) -->
                    <form action="${pageContext.request.contextPath}/patient/rendez-vous" method="post">
                        <input type="hidden" name="action" value="creer">

                        <div class="row mb-2">
                            <div class="col-md-4">
                                <label>Médecin :</label>
                                <select name="medecinId" class="form-select" required>
                                    <option value="">-- Choisir un médecin --</option>
                                    <c:forEach items="${medecins}" var="m">
                                        <option value="${m.id}">
                                            Dr. ${m.prenom} ${m.nom} (${m.specialite.nom})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label>Date souhaitée (Lundi au Vendredi) :</label>
                                <input type="date" name="date" class="form-control" required>
                            </div>
                            <div class="col-md-4">
                                <label>Créneau horaire (30 min) :</label>
                                <select name="heureDebut" class="form-select" required>
                                    <option value="">-- Choisir un créneau --</option>
                                    <option value="08:00">08:00 - 08:30</option>
                                    <option value="08:30">08:30 - 09:00</option>
                                    <option value="09:00">09:00 - 09:30</option>
                                    <option value="09:30">09:30 - 10:00</option>
                                    <option value="10:00">10:00 - 10:30</option>
                                    <option value="10:30">10:30 - 11:00</option>
                                    <option value="11:00">11:00 - 11:30</option>
                                    <option value="11:30">11:30 - 12:00</option>
                                    <option value="12:00">12:00 - 12:30</option>
                                    <option value="12:30">12:30 - 13:00</option>
                                    <option value="13:00">13:00 - 13:30</option>
                                    <option value="13:30">13:30 - 14:00</option>
                                    <option value="14:00">14:00 - 14:30</option>
                                    <option value="14:30">14:30 - 15:00</option>
                                    <option value="15:00">15:00 - 15:30</option>
                                    <option value="15:30">15:30 - 16:00</option>
                                    <option value="16:00">16:00 - 16:30</option>
                                    <option value="16:30">16:30 - 17:00</option>
                                    <option value="17:00">17:00 - 17:30</option>
                                    <option value="17:30">17:30 - 18:00</option>
                                </select>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label>Motif de consultation :</label>
                            <textarea name="motif" class="form-control" rows="2" placeholder="Décrivez brièvement le motif de votre consultation..." required></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary">Réserver</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Tableau des rendez-vous du patient -->
        <h4>Historique de mes Rendez-Vous</h4>
        <table class="table table-bordered table-striped">
            <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Date & Créneau</th>
                    <th>Médecin</th>
                    <th>Spécialité</th>
                    <th>Motif</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty mesRendezVous}">
                    <tr>
                        <td colspan="7" class="text-center">Vous n'avez aucun rendez-vous enregistré.</td>
                    </tr>
                </c:if>
                <c:forEach items="${mesRendezVous}" var="r">
                    <tr>
                        <td>${r.id}</td>
                        <td>
                            <strong>${r.date}</strong>
                            <c:if test="${not empty r.heureDebut}">
                                <br><small class="text-primary font-monospace">${r.heureDebut} - ${r.heureFin}</small>
                            </c:if>
                        </td>
                        <td>Dr. ${r.medecin.prenom} ${r.medecin.nom}</td>
                        <td>${r.medecin.specialite.nom}</td>
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
                                <c:when test="${r.status == 'EN_ATTENTE' || r.status == 'CONFIRME'}">
                                    <a href="${pageContext.request.contextPath}/patient/rendez-vous?action=reporter&id=${r.id}" class="btn btn-sm btn-warning">Reporter</a>
                                    <a href="${pageContext.request.contextPath}/patient/rendez-vous?action=annuler&id=${r.id}" class="btn btn-sm btn-danger" onclick="return confirm('Annuler ce rendez-vous ?');">Annuler</a>
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
