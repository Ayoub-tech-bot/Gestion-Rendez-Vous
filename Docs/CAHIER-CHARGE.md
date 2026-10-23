# Gestion des rendez-vous médicaux


## Objectif

Aujourd'hui, beaucoup de cabinets gèrent encore les rendez-vous par téléphone ou sur papier : double réservation, oublis, temps perdu. On veut une petite application web où le patient réserve en ligne, le médecin valide, et l'administrateur gère les médecins et les spécialités.


## Acteurs

| Acteur | Ce qu'il fait |
|---|---|
| Visiteur | Crée un compte patient ou se connecte |
| Patient | Cherche un médecin, prend / reporte / annule ses rendez-vous |
| Médecin | Voit ses rendez-vous, les confirme, les refuse ou les termine |
| Administrateur | Gère les spécialités et les médecins, consulte tous les rendez-vous |

## Fonctionnalités

- Inscription et connexion (accès selon le rôle)
- CRUD des spécialités (administrateur)
- CRUD des médecins (administrateur)
- Prendre, consulter, reporter et annuler un rendez-vous (patient)
- Confirmer, refuser et terminer un rendez-vous (médecin)

## Règles à respecter

1. Un médecin n'a pas deux rendez-vous actifs au même créneau.
2. Pas de rendez-vous dans le passé.
3. Créneaux de 30 min, de 08h à 18h, du lundi au vendredi.
4. Un patient ne modifie ou n'annule que ses propres rendez-vous, s'ils sont à venir.
5. Seul le médecin concerné peut confirmer, refuser ou terminer un rendez-vous.
6. On ne supprime pas un médecin qui a des rendez-vous actifs, ni une spécialité qui a des médecins.

## Cycle de vie d'un rendez-vous

```
EN_ATTENTE ──► CONFIRME ──► TERMINE
    │             │
    ├──► REFUSE   └──► ANNULE
    └──► ANNULE
```

Le patient demande, le médecin décide. Un rendez-vous refusé, annulé ou terminé ne bouge plus.

## Scénarios

### S1 — Inscription
**Acteur :** Visiteur
1. Remplit le formulaire (nom, prénom, email, téléphone, mot de passe).
2. Le système vérifie les champs et que l'email n'existe pas déjà.
3. Le compte patient est créé, retour à la page de connexion.

*Si l'email existe déjà ou qu'un champ est invalide : message d'erreur, rien n'est créé.*

### S2 — Connexion
**Acteur :** tout utilisateur
1. Saisit email et mot de passe.
2. Le système vérifie les identifiants et ouvre une session.
3. Redirection vers l'accueil du rôle (patient, médecin ou admin).

*Si les identifiants sont faux : retour à la connexion avec un message. Une page protégée sans session renvoie aussi vers la connexion.*

### S3 — Gérer les spécialités
**Acteur :** Administrateur
1. Ouvre la liste des spécialités.
2. Ajoute, modifie ou supprime une spécialité.
3. La liste est mise à jour.

*Suppression refusée si des médecins y sont rattachés.*

### S4 — Gérer les médecins
**Acteur :** Administrateur
1. Ouvre la liste des médecins.
2. Crée un médecin (identité, email, mot de passe, cabinet, spécialité), ou modifie / supprime un existant.
3. Le médecin peut ensuite se connecter.

*Suppression refusée si le médecin a des rendez-vous actifs. Email déjà utilisé : création refusée.*

### S5 — Prendre un rendez-vous
**Acteur :** Patient (connecté)
1. Choisit une spécialité et voit les médecins correspondants.
2. Choisit un médecin et une date : le système affiche les créneaux libres.
3. Choisit un créneau, écrit le motif.
4. Le rendez-vous est créé au statut `EN_ATTENTE`.

*Si le créneau vient d'être pris par quelqu'un d'autre, ou si le patient a déjà un rendez-vous à cette heure : refus avec message, les créneaux sont rechargés.*

### S6 — Reporter ou annuler un rendez-vous
**Acteur :** Patient
1. Ouvre « Mes rendez-vous ».
2. Annule un rendez-vous à venir → statut `ANNULE`, le créneau redevient libre.
3. Ou le reporte : choisit un nouveau créneau libre, le statut repasse à `EN_ATTENTE`.

*Impossible si le rendez-vous est passé, refusé, terminé ou déjà annulé. Si le nouveau créneau est pris, l'ancien rendez-vous reste tel quel.*

### S7 — Répondre à une demande
**Acteur :** Médecin
1. Ouvre ses rendez-vous en attente.
2. Confirme ou refuse une demande.
3. Le patient voit le nouveau statut dans « Mes rendez-vous ».

*Un refus libère le créneau.*

### S8 — Terminer une consultation
**Acteur :** Médecin
1. Ouvre son agenda du jour.
2. Choisit un rendez-vous confirmé et clique sur « Terminer » → statut `TERMINE`.

### S9 — Superviser
**Acteur :** Administrateur
1. Ouvre la liste de tous les rendez-vous, filtrable par médecin, date ou statut.
2. Peut aussi consulter la liste des patients.

*Consultation seulement, aucune modification.*

## Modèle de données

Entités Hibernate :

- **Utilisateur** (classe mère) : id, nom, prénom, email (unique), motDePasse, rôle
  - **Patient** : téléphone, dateNaissance
  - **Médecin** : cabinet, spécialité
  - **Administrateur**
- **Spécialité** : id, nom, description
- **RendezVous** : id, date, heureDebut, heureFin, statut, motif, patient, médecin

Relations :

- Une spécialité a plusieurs médecins (`OneToMany` / `ManyToOne`)
- Un médecin a plusieurs rendez-vous (`OneToMany` / `ManyToOne`)
- Un patient a plusieurs rendez-vous (`OneToMany` / `ManyToOne`)

## Technique

- Java, Servlets + JSP/JSTL, architecture MVC
- Hibernate (annotations), tables générées automatiquement
- MySQL, Tomcat, Maven
- Mots de passe hachés (BCrypt), filtre qui contrôle l'accès selon le rôle
- UML sous Astah, versionnement Git / GitHub

Organisation du code : `model` (entités) → `dao` (Hibernate) → `service` (règles) → `servlet` (contrôleurs) → `webapp/WEB-INF/views` (JSP).

