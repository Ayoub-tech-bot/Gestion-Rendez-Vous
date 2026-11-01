package ma.ensa.gestion.rdv.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Medecin extends Utilisateur{

    @ManyToOne
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;

    @OneToMany(mappedBy = "medecin")
    private List<RendezVous> rendezVous = new ArrayList<>();

    public Medecin() {

    }

    public Medecin(long id, String nom, String prenom, String email, String motDePasse, Role role, Specialite specialite, List<RendezVous> rendezVous) {
        super(id, nom, prenom, email, motDePasse, role);
        this.specialite = specialite;
        this.rendezVous = rendezVous;
    }
}
