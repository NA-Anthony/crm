package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_budget")
    private Integer idBudget;

    @Column(name = "montant", nullable = false)
    private Double montant;

    @ManyToOne
    @JoinColumn(name = "id_taux", nullable = false)
    private TauxAlerte tauxAlerte;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    // Constructeurs, getters et setters

    public Budget() {
    }

    public Budget(Double montant, TauxAlerte tauxAlerte, Customer customer) {
        this.montant = montant;
        this.tauxAlerte = tauxAlerte;
        this.customer = customer;
    }

    public Integer getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(Integer idBudget) {
        this.idBudget = idBudget;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public TauxAlerte getTauxAlerte() {
        return tauxAlerte;
    }

    public void setTauxAlerte(TauxAlerte tauxAlerte) {
        this.tauxAlerte = tauxAlerte;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}