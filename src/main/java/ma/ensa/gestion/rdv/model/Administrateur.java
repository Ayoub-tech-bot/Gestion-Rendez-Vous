package ma.ensa.gestion.rdv.model;

import jakarta.persistence.Entity;

import javax.management.relation.Role;

@Entity
public class Administrateur extends Utilisateur {

    public Administrateur() {}

    public Administrateur(long id, String nom, String prenom, String email, String motDePasse, Role role) {
        super(id, nom, prenom, email, motDePasse, role);
    }
}
