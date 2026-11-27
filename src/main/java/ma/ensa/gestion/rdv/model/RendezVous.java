package ma.ensa.gestion.rdv.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    @Enumerated(EnumType.STRING)
    private StatutRendezVous status;
    private String motif;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    public RendezVous() {
    }

    public RendezVous(long id, LocalDate date, StatutRendezVous status, String motif) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.motif = motif;
    }

    public RendezVous(long id, LocalDate date, LocalTime heureDebut, LocalTime heureFin, StatutRendezVous status, String motif) {
        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.status = status;
        this.motif = motif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public StatutRendezVous getStatus() {
        return status;
    }

    public void setStatus(StatutRendezVous status) {
        this.status = status;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", date=" + date +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", status=" + status +
                ", motif='" + motif + '\'' +
                '}';
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
    
}
