package ma.ensa.gestion.rdv.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
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
                ", status=" + status +
                ", motif='" + motif + '\'' +
                '}';
    }
}
