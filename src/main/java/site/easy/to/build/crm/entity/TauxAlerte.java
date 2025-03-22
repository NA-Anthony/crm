package site.easy.to.build.crm.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "taux_alerte")
public class TauxAlerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taux")
    private Integer idTaux;

    @Column(name = "taux", nullable = false)
    private Double taux;

    // Constructeurs, getters et setters

    public TauxAlerte() {
    }

    public TauxAlerte(Double taux) {
        this.taux = taux;
    }

    public Integer getIdTaux() {
        return idTaux;
    }

    public void setIdTaux(Integer idTaux) {
        this.idTaux = idTaux;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }
}